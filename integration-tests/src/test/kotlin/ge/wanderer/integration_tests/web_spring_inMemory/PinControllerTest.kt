package ge.wanderer.integration_tests.web_spring_inMemory

import ge.wanderer.common.constants.TRANSIENT_ID
import ge.wanderer.common.enums.PinType
import ge.wanderer.common.enums.ReportReason
import ge.wanderer.common.functions.fromJson
import ge.wanderer.common.functions.toJson
import ge.wanderer.common.map.LatLng
import ge.wanderer.common.now
import ge.wanderer.integration_tests.DEFAULT_LISTING_PARAMS
import ge.wanderer.integration_tests.SpringWebAppWithInMemoryPersistence
import ge.wanderer.integration_tests.get
import ge.wanderer.integration_tests.post
import ge.wanderer.service.protocol.data.*
import ge.wanderer.service.protocol.request.*
import ge.wanderer.service.protocol.response.ServiceListingResponse
import ge.wanderer.service.protocol.response.ServiceResponse
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestPropertySource
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@SpringBootTest(classes = [SpringWebAppWithInMemoryPersistence::class], webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@TestPropertySource("classpath:service-spring-test.properties")
class PinControllerTest(
    @Autowired private val mockMvc: MockMvc
) {

    private val controllerPath = "/api/pins/"

    @Test
    fun getsPinById() {
        val pinId = 1L
        mockMvc.get(controllerPath + pinId)
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.isSuccessful").value(true))
            .andExpect(jsonPath("$.message").value("Pin fetched!"))
            .andExpect(jsonPath("$.data.id").value(pinId))
    }

    @Test
    fun listsPins() {
        val responseString = mockMvc.post(controllerPath + "list", toJson(DEFAULT_LISTING_PARAMS))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.isSuccessful").value(true))
            .andExpect(jsonPath("$.message").value("Pins Fetched!"))
            .andReturn()
            .response.contentAsString
        val responseData = fromJson<ServiceListingResponse<PinData>>(responseString).data
        assertTrue(responseData.isNotEmpty())
    }

    @Test
    fun listsPinsForGivenRoute() {
        val responseString = mockMvc.post(controllerPath + "for-route/TB201301", toJson(DEFAULT_LISTING_PARAMS))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.isSuccessful").value(true))
            .andExpect(jsonPath("$.message").value("Pins Fetched!"))
            .andReturn()
            .response.contentAsString

        val responseData = fromJson<ServiceListingResponse<PinMapData>>(responseString).data
        assertTrue(responseData.isNotEmpty())
        assertTrue(responseData.all { it.routeCode == "TB201301" })
    }

    @Test
    fun reportsAsIrrelevant() {
        val pinId = 1L

        mockMvc.post(controllerPath + "report-irrelevant", toJson(OperateOnContentRequest(pinId, 2, now())))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.isSuccessful").value(true))
            .andExpect(jsonPath("$.message").value("Content Reported!"))
            .andExpect(jsonPath("$.data.id").value(pinId))
            .andExpect(jsonPath("$.data.isRelevant").value(true))

        mockMvc.post(controllerPath + "report-irrelevant", toJson(OperateOnContentRequest(pinId, 3, now())))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.data.isRelevant").value(true))

        mockMvc.post(controllerPath + "report-irrelevant", toJson(OperateOnContentRequest(pinId, 3, now())))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.message").value("You already reported this content"))

        mockMvc.post(controllerPath + "report-irrelevant", toJson(OperateOnContentRequest(pinId, 4, now())))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.data.isRelevant").value(false))
            .andExpect(jsonPath("$.data.isActive").value(false))
            .andExpect(jsonPath("$.data.id").value(pinId))
    }

    @Test
    fun createsAndUpdatesPin() {
        val request = CreatePinRequest(now(), 1, PinType.TIP, "Some title", "aaaaa", null, LatLng(1f, 2f), "1234")

        val responseString = mockMvc.post(controllerPath + "create", toJson(request))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.isSuccessful").value(true))
            .andExpect(jsonPath("$.message").value("Pin Created. New model persisted successfully"))
            .andExpect(jsonPath("$.data.id").isNotEmpty)
            .andExpect(jsonPath("$.data.attachedFile").isEmpty)
            .andExpect(jsonPath("$.data.text").value("aaaaa"))
            .andReturn()
            .response.contentAsString
        val newPinId = fromJson<ServiceResponse<PinData>>(responseString).data!!.id

        val updateRequest = UpdatePinRequest(newPinId, "Some title", "Some text", FileData(), 1)
        mockMvc.post(controllerPath + "update", toJson(updateRequest))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.isSuccessful").value(true))
            .andExpect(jsonPath("$.message").value("Pin updated"))
            .andExpect(jsonPath("$.data.id").value(newPinId))
            .andExpect(jsonPath("$.data.attachedFile").isNotEmpty)
            .andExpect(jsonPath("$.data.text").value("Some text"))
    }

    @Test
    fun removesAndActivatesPin() {
        val pinId = 2L

        mockMvc.post(controllerPath + "remove", toJson(OperateOnContentRequest(pinId, 1, now())))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.isSuccessful").value(true))
            .andExpect(jsonPath("$.message").value("PIN removed successfully!"))
            .andExpect(jsonPath("$.data.id").value(pinId))
            .andExpect(jsonPath("$.data.isActive").value(false))
            .andExpect(jsonPath("$.data.isRemoved").value(true))

        mockMvc.post(controllerPath + "activate", toJson(OperateOnContentRequest(pinId, 1, now())))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.isSuccessful").value(true))
            .andExpect(jsonPath("$.message").value("PIN activated successfully!"))
            .andExpect(jsonPath("$.data.id").value(pinId))
            .andExpect(jsonPath("$.data.isActive").value(true))
            .andExpect(jsonPath("$.data.isRemoved").value(false))
    }

    @Test
    fun ratesPin() {
        val pinId = 1L

        mockMvc.post(controllerPath + "up-vote", toJson(OperateOnContentRequest(pinId, 2, now())))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.isSuccessful").value(true))
            .andExpect(jsonPath("$.message").value("Vote added"))
            .andExpect(jsonPath("$.data.totalRating").value(10))

        mockMvc.post(controllerPath + "down-vote", toJson(OperateOnContentRequest(pinId, 2, now())))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.isSuccessful").value(true))
            .andExpect(jsonPath("$.message").value("Vote added"))
            .andExpect(jsonPath("$.data.totalRating").value(-10))

        mockMvc.post(controllerPath + "remove-vote", toJson(OperateOnContentRequest(pinId, 2, now())))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.isSuccessful").value(true))
            .andExpect(jsonPath("$.message").value("Vote Removed"))
            .andExpect(jsonPath("$.data.totalRating").value(0))
    }

    @Test
    fun commentsPin() {
        val pinId = 1L

        val request = AddCommentRequest(pinId, 2, "Some comment", now())
        mockMvc.post(controllerPath + "add-comment", toJson(request))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.isSuccessful").value(true))
            .andExpect(jsonPath("$.message").value("Comment added"))
            .andExpect(jsonPath("$.data.text").value("Some comment"))
            .andExpect(jsonPath("$.data.author.id").value(2))

        val commentsResponseString = mockMvc.post(controllerPath + "1/comments", toJson(DEFAULT_LISTING_PARAMS))
            .andExpect(status().isOk)
            .andReturn().response.contentAsString

        val comments = fromJson<ServiceListingResponse<CommentData>>(commentsResponseString).data
        assertTrue { comments.any { it.text == "Some comment" && it.author.id == 2L && it.id != TRANSIENT_ID } }
    }

    @Test
    fun reportsPin() {
        mockMvc.post(controllerPath + "report", toJson(ReportContentRequest(1, 5, now(), ReportReason.INAPPROPRIATE_CONTENT)))
            .andExpect(status().isOk)

        mockMvc.post(controllerPath + "report", toJson(ReportContentRequest(1, 6, now(), ReportReason.INAPPROPRIATE_CONTENT)))
            .andExpect(status().isOk)

        mockMvc.post(controllerPath + "report", toJson(ReportContentRequest(1, 7, now(), ReportReason.INAPPROPRIATE_CONTENT)))
            .andExpect(status().isOk)

        val reportsString = mockMvc.post(controllerPath + "1/reports", toJson(DEFAULT_LISTING_PARAMS))
            .andExpect(status().isOk)
            .andReturn().response.contentAsString

        val reports = fromJson<ServiceListingResponse<ReportData>>(reportsString).data
        assertEquals(3, reports.filter { it.reason == ReportReason.INAPPROPRIATE_CONTENT }.size)
    }

}