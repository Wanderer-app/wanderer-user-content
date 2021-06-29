package ge.wanderer.integration_tests.web_spring_inMemory

import ge.wanderer.common.constants.TRANSIENT_ID
import ge.wanderer.common.enums.FileType
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
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
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
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.isSuccessful").value(true))
            .andExpect(jsonPath("$.message").value("Post fetched"))
            .andExpect(jsonPath("$.data.id").value(postId))
    }

    @Test
    fun updatesPost() {
        val postId = 1L
        val request = UpdatePostRequest(1, "New text", listOf(FileData("1", FileType.IMAGE), FileData("1", FileType.IMAGE)), "5760b116-6aab-4f04-b8be-650e27a85d09")

        mockMvc.post(controllerPath + "update", toJson(request))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.isSuccessful").value(true))
            .andExpect(jsonPath("$.message").value("POST updated"))
            .andExpect(jsonPath("$.data.id").value(postId))
            .andExpect(jsonPath("$.data.content").value("New text"))
            .andExpect(jsonPath("$.data.attachedFiles").isNotEmpty)
    }

    @Test
    fun createsPost() {
        val request = CreatePostRequest(now(), "5760b116-6aab-4f04-b8be-650e27a85d09", "1234", "Some text", listOf(FileData("1", FileType.IMAGE)))

        mockMvc.post(controllerPath + "create", toJson(request))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.isSuccessful").value(true))
            .andExpect(jsonPath("$.data.content").value("Some text"))
            .andExpect(jsonPath("$.data.attachedFiles").isNotEmpty)
    }

    @Test
    fun removesAndActivatesPost() {
        val postId = 1L

        mockMvc.post(controllerPath + "remove", toJson(OperateOnContentRequest(postId, "85fa0681-b7bd-4ee3-b5b5-eb2672181ae2", now())))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.isSuccessful").value(true))
            .andExpect(jsonPath("$.message").value("POST removed successfully!"))
            .andExpect(jsonPath("$.data.id").value(postId))
            .andExpect(jsonPath("$.data.isActive").value(false))
            .andExpect(jsonPath("$.data.isRemoved").value(true))

        mockMvc.post(controllerPath + "activate", toJson(OperateOnContentRequest(postId, "85fa0681-b7bd-4ee3-b5b5-eb2672181ae2", now())))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.isSuccessful").value(true))
            .andExpect(jsonPath("$.message").value("POST activated successfully!"))
            .andExpect(jsonPath("$.data.id").value(postId))
            .andExpect(jsonPath("$.data.isActive").value(true))
            .andExpect(jsonPath("$.data.isRemoved").value(false))
    }

    @Test
    fun ratesPost() {
        val postId = 1L

        mockMvc.post(controllerPath + "up-vote", toJson(OperateOnContentRequest(postId, "85fa0681-b7bd-4ee3-b5b5-eb2672181ae2", now())))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.isSuccessful").value(true))
            .andExpect(jsonPath("$.message").value("Vote added"))
            .andExpect(jsonPath("$.data.totalRating").value(1))

        mockMvc.post(controllerPath + "down-vote", toJson(OperateOnContentRequest(postId, "85fa0681-b7bd-4ee3-b5b5-eb2672181ae2", now())))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.isSuccessful").value(true))
            .andExpect(jsonPath("$.message").value("Vote added"))
            .andExpect(jsonPath("$.data.totalRating").value(-1))

        mockMvc.post(controllerPath + "remove-vote", toJson(OperateOnContentRequest(postId, "85fa0681-b7bd-4ee3-b5b5-eb2672181ae2", now())))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.isSuccessful").value(true))
            .andExpect(jsonPath("$.message").value("Vote Removed"))
            .andExpect(jsonPath("$.data.totalRating").value(0))
    }

    @Test
    fun commentsPost() {
        val postId = 1L

        val request = AddCommentRequest(postId, "85fa0681-b7bd-4ee3-b5b5-eb2672181ae2", "Some comment", now())
        mockMvc.post(controllerPath + "add-comment", toJson(request))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.isSuccessful").value(true))
            .andExpect(jsonPath("$.message").value("Comment added"))
            .andExpect(jsonPath("$.data.text").value("Some comment"))
            .andExpect(jsonPath("$.data.author.id").value("85fa0681-b7bd-4ee3-b5b5-eb2672181ae2"))

        val commentsResponseString = mockMvc.post(controllerPath + "1/comments", toJson(DEFAULT_LISTING_PARAMS))
            .andExpect(status().isOk)
            .andReturn().response.contentAsString

        val comments = fromJson<ServiceListingResponse<CommentData>>(commentsResponseString).data
        assertTrue { comments.any { it.text == "Some comment" && it.author.id == "85fa0681-b7bd-4ee3-b5b5-eb2672181ae2" && it.id != TRANSIENT_ID } }
    }

    @Test
    fun reportsPost() {
        val postId: Long = 2
        mockMvc.post(controllerPath + "report", toJson(ReportContentRequest(postId, "85fa0681-b7bd-4ee3-b5b5-eb2672181ae2", now(), ReportReason.INAPPROPRIATE_CONTENT)))
            .andExpect(status().isOk)

        mockMvc.post(controllerPath + "report", toJson(ReportContentRequest(postId, "04e51444-85af-4d92-b89a-c8f761b7f3ea", now(), ReportReason.INAPPROPRIATE_CONTENT)))
            .andExpect(status().isOk)

        mockMvc.post(controllerPath + "report", toJson(ReportContentRequest(postId, "b41c2dd8-db85-4d96-a1f4-92f90851f7f2", now(), ReportReason.INAPPROPRIATE_CONTENT)))
            .andExpect(status().isOk)

        val reportsString = mockMvc.post(controllerPath + "${postId}/reports", toJson(DEFAULT_LISTING_PARAMS))
            .andExpect(status().isOk)
            .andReturn().response.contentAsString

        val reports = fromJson<ServiceListingResponse<ReportData>>(reportsString).data
        assertEquals(3, reports.size)
    }

}