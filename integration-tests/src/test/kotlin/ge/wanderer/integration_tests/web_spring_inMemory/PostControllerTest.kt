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
import ge.wanderer.service.protocol.data.FileData
import ge.wanderer.service.protocol.data.ReportData
import ge.wanderer.service.protocol.request.*
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
class PostControllerTest(
    @Autowired private val mockMvc: MockMvc
) {

    private val controllerPath = "/api/posts/"

    @Test
    fun findsPostById() {
        val postId = 1L
        mockMvc.get(controllerPath + postId)
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.isSuccessful").value(true))
            .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Post fetched"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data.id").value(postId))
    }

    @Test
    fun updatesPost() {
        val postId = 1L
        val request = UpdatePostRequest(1, "New text", listOf(FileData(), FileData()), 1)

        mockMvc.post(controllerPath + "update", toJson(request))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.isSuccessful").value(true))
            .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("POST updated"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data.id").value(postId))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data.content").value("New text"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data.attachedFiles").isNotEmpty)
    }

    @Test
    fun createsPost() {
        val request = CreatePostRequest(now(), 1, "1234", "Some text", listOf(FileData()))

        mockMvc.post(controllerPath + "create", toJson(request))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.isSuccessful").value(true))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data.content").value("Some text"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data.attachedFiles").isNotEmpty)
    }

    @Test
    fun removesAndActivatesPost() {
        val postId = 1L

        mockMvc.post(controllerPath + "remove", toJson(OperateOnContentRequest(postId, 2, now())))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.isSuccessful").value(true))
            .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("POST removed successfully!"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data.id").value(postId))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data.isActive").value(false))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data.isRemoved").value(true))

        mockMvc.post(controllerPath + "activate", toJson(OperateOnContentRequest(postId, 2, now())))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.isSuccessful").value(true))
            .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("POST activated successfully!"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data.id").value(postId))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data.isActive").value(true))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data.isRemoved").value(false))
    }

    @Test
    fun ratesPost() {
        val postId = 1L

        mockMvc.post(controllerPath + "up-vote", toJson(OperateOnContentRequest(postId, 2, now())))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.isSuccessful").value(true))
            .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Vote added"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data.totalRating").value(1))

        mockMvc.post(controllerPath + "down-vote", toJson(OperateOnContentRequest(postId, 2, now())))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.isSuccessful").value(true))
            .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Vote added"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data.totalRating").value(-1))

        mockMvc.post(controllerPath + "remove-vote", toJson(OperateOnContentRequest(postId, 2, now())))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.isSuccessful").value(true))
            .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Vote Removed"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data.totalRating").value(0))
    }

    @Test
    fun commentsPost() {
        val postId = 1L

        val request = AddCommentRequest(postId, 2, "Some comment", now())
        mockMvc.post(controllerPath + "add-comment", toJson(request))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.isSuccessful").value(true))
            .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Comment added"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data.text").value("Some comment"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data.author.id").value(2))

        val commentsResponseString = mockMvc.post(controllerPath + "1/comments", toJson(DEFAULT_LISTING_PARAMS))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andReturn().response.contentAsString

        val comments = fromJson<ServiceListingResponse<CommentData>>(commentsResponseString).data
        assertTrue { comments.any { it.text == "Some comment" && it.author.id == 2L && it.id != TRANSIENT_ID } }
    }

    @Test
    fun reportsPost() {
        val postId: Long = 2
        mockMvc.post(controllerPath + "report", toJson(ReportContentRequest(postId, 2, now(), ReportReason.INAPPROPRIATE_CONTENT)))
            .andExpect(MockMvcResultMatchers.status().isOk)

        mockMvc.post(controllerPath + "report", toJson(ReportContentRequest(postId, 3, now(), ReportReason.INAPPROPRIATE_CONTENT)))
            .andExpect(MockMvcResultMatchers.status().isOk)

        mockMvc.post(controllerPath + "report", toJson(ReportContentRequest(postId, 4, now(), ReportReason.INAPPROPRIATE_CONTENT)))
            .andExpect(MockMvcResultMatchers.status().isOk)

        val reportsString = mockMvc.post(controllerPath + "${postId}/reports", toJson(DEFAULT_LISTING_PARAMS))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andReturn().response.contentAsString

        val reports = fromJson<ServiceListingResponse<ReportData>>(reportsString).data
        assertEquals(3, reports.size)
    }

}