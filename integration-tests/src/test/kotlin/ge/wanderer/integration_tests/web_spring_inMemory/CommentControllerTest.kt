package ge.wanderer.integration_tests.web_spring_inMemory

import ge.wanderer.common.constants.TRANSIENT_ID
import ge.wanderer.common.enums.ReportReason
import ge.wanderer.common.functions.fromJson
import ge.wanderer.common.functions.toJson
import ge.wanderer.common.now
import ge.wanderer.integration_tests.DEFAULT_LISTING_PARAMS
import ge.wanderer.integration_tests.SpringWebAppWithInMemoryPersistence
import ge.wanderer.integration_tests.get
import ge.wanderer.integration_tests.post
import ge.wanderer.service.protocol.data.CommentData
import ge.wanderer.service.protocol.data.ReportData
import ge.wanderer.service.protocol.request.AddCommentRequest
import ge.wanderer.service.protocol.request.OperateOnContentRequest
import ge.wanderer.service.protocol.request.ReportContentRequest
import ge.wanderer.service.protocol.request.UpdateCommentRequest
import ge.wanderer.service.protocol.response.ServiceListingResponse
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestPropertySource
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@SpringBootTest(classes = [SpringWebAppWithInMemoryPersistence::class], webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@TestPropertySource("classpath:service-spring-test.properties")
class CommentControllerTest(
    @Autowired private val mockMvc: MockMvc

) {
    private val controllerPath = "/api/comments/"

    @Test
    fun findsCommentById() {
        val commentId = 1L
        mockMvc.get(controllerPath + commentId)
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.isSuccessful").value(true))
            .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Successfully retrieved comment"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data.id").value(commentId))
    }

    @Test
    fun updatesComment() {
        mockMvc.post(controllerPath + "update", toJson(UpdateCommentRequest(1, "2", "Updated text")))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.isSuccessful").value(true))
            .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Comment updated"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data.text").value("Updated text"))
    }

    @Test
    fun activatesAndRemovesComment() {
        val commentId = 1L
        mockMvc.post(controllerPath + "remove", toJson(OperateOnContentRequest(commentId, "2", now())))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.isSuccessful").value(true))
            .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("COMMENT removed successfully!"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data.id").value(commentId))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data.isActive").value(false))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data.isRemoved").value(true))

        mockMvc.post(controllerPath + "activate", toJson(OperateOnContentRequest(commentId, "2", now())))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.isSuccessful").value(true))
            .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("COMMENT activated successfully!"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data.id").value(commentId))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data.isActive").value(true))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data.isRemoved").value(false))
    }

    @Test
    fun ratesComment() {
        val commentId = 1L

        mockMvc.post(controllerPath + "up-vote", toJson(OperateOnContentRequest(commentId, "1", now())))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.isSuccessful").value(true))
            .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Vote added"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data.totalRating").value(1))

        mockMvc.post(controllerPath + "down-vote", toJson(OperateOnContentRequest(commentId, "1", now())))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.isSuccessful").value(true))
            .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Vote added"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data.totalRating").value(-1))

        mockMvc.post(controllerPath + "remove-vote", toJson(OperateOnContentRequest(commentId, "1", now())))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.isSuccessful").value(true))
            .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Vote Removed"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data.totalRating").value(0))
    }

    @Test
    fun addsReplies() {
        val commentId = 1L

        val request = AddCommentRequest(commentId, "2", "Some comment", now())
        mockMvc.post(controllerPath + "add-reply", toJson(request))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.isSuccessful").value(true))
            .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Comment added"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data.text").value("Some comment"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data.author.id").value(2))

        val commentsResponseString = mockMvc.post(controllerPath + "1/replies", toJson(DEFAULT_LISTING_PARAMS))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andReturn().response.contentAsString

        val comments = fromJson<ServiceListingResponse<CommentData>>(commentsResponseString).data
        assertTrue { comments.any { it.text == "Some comment" && it.author.id == "2" && it.id != TRANSIENT_ID } }
    }

    @Test
    fun reportsComment() {
        mockMvc.post(controllerPath + "report", toJson(ReportContentRequest(1, "2", now(), ReportReason.INAPPROPRIATE_CONTENT)))
            .andExpect(MockMvcResultMatchers.status().isOk)

        mockMvc.post(controllerPath + "report", toJson(ReportContentRequest(1, "3", now(), ReportReason.INAPPROPRIATE_CONTENT)))
            .andExpect(MockMvcResultMatchers.status().isOk)

        mockMvc.post(controllerPath + "report", toJson(ReportContentRequest(1, "4", now(), ReportReason.INAPPROPRIATE_CONTENT)))
            .andExpect(MockMvcResultMatchers.status().isOk)

        val reportsString = mockMvc.post(controllerPath + "1/reports", toJson(DEFAULT_LISTING_PARAMS))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andReturn().response.contentAsString

        val reports = fromJson<ServiceListingResponse<ReportData>>(reportsString).data
        assertEquals(3, reports.size)
    }
}