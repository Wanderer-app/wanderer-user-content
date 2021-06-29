package ge.wanderer.integration_tests.web_spring_inMemory

import ge.wanderer.common.constants.TRANSIENT_ID
import ge.wanderer.common.enums.FileType
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

        mockMvc.post(controllerPath + "report-irrelevant", toJson(OperateOnContentRequest(pinId, "85fa0681-b7bd-4ee3-b5b5-eb2672181ae2", now())))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.isSuccessful").value(true))
            .andExpect(jsonPath("$.message").value("Content Reported!"))
            .andExpect(jsonPath("$.data.id").value(pinId))
            .andExpect(jsonPath("$.data.isRelevant").value(true))

        mockMvc.post(controllerPath + "report-irrelevant", toJson(OperateOnContentRequest(pinId, "04e51444-85af-4d92-b89a-c8f761b7f3ea", now())))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.data.isRelevant").value(true))

        mockMvc.post(controllerPath + "report-irrelevant", toJson(OperateOnContentRequest(pinId, "04e51444-85af-4d92-b89a-c8f761b7f3ea", now())))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.message").value("You already reported this content"))

        mockMvc.post(controllerPath + "report-irrelevant", toJson(OperateOnContentRequest(pinId, "b41c2dd8-db85-4d96-a1f4-92f90851f7f2", now())))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.data.isRelevant").value(false))
            .andExpect(jsonPath("$.data.isActive").value(false))
            .andExpect(jsonPath("$.data.id").value(pinId))
    }

    @Test
    fun createsAndUpdatesPin() {
        val request = CreatePinRequest(now(), "5760b116-6aab-4f04-b8be-650e27a85d09", PinType.TIP, "Some title", "aaaaa", null, LatLng(1f, 2f), "1234")

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

        val updateRequest = UpdatePinRequest(newPinId, "Some title", "Some text", FileData("5760b116-6aab-4f04-b8be-650e27a85d09", FileType.IMAGE), "5760b116-6aab-4f04-b8be-650e27a85d09")
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

        mockMvc.post(controllerPath + "remove", toJson(OperateOnContentRequest(pinId, "5760b116-6aab-4f04-b8be-650e27a85d09", now())))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.isSuccessful").value(true))
            .andExpect(jsonPath("$.message").value("PIN removed successfully!"))
            .andExpect(jsonPath("$.data.id").value(pinId))
            .andExpect(jsonPath("$.data.isActive").value(false))
            .andExpect(jsonPath("$.data.isRemoved").value(true))

        mockMvc.post(controllerPath + "activate", toJson(OperateOnContentRequest(pinId, "5760b116-6aab-4f04-b8be-650e27a85d09", now())))
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

        mockMvc.post(controllerPath + "up-vote", toJson(OperateOnContentRequest(pinId, "85fa0681-b7bd-4ee3-b5b5-eb2672181ae2", now())))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.isSuccessful").value(true))
            .andExpect(jsonPath("$.message").value("Vote added"))
            .andExpect(jsonPath("$.data.totalRating").value(10))

        mockMvc.post(controllerPath + "down-vote", toJson(OperateOnContentRequest(pinId, "85fa0681-b7bd-4ee3-b5b5-eb2672181ae2", now())))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.isSuccessful").value(true))
            .andExpect(jsonPath("$.message").value("Vote added"))
            .andExpect(jsonPath("$.data.totalRating").value(-10))

        mockMvc.post(controllerPath + "remove-vote", toJson(OperateOnContentRequest(pinId, "85fa0681-b7bd-4ee3-b5b5-eb2672181ae2", now())))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.isSuccessful").value(true))
            .andExpect(jsonPath("$.message").value("Vote Removed"))
            .andExpect(jsonPath("$.data.totalRating").value(0))
    }

    @Test
    fun commentsPin() {
        val pinId = 1L

        val request = AddCommentRequest(pinId, "85fa0681-b7bd-4ee3-b5b5-eb2672181ae2", "Some comment", now())
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
    fun reportsPin() {
        mockMvc.post(controllerPath + "report", toJson(ReportContentRequest(1, "755520ef-f06a-49e2-af7e-a0f4c19b1aba", now(), ReportReason.INAPPROPRIATE_CONTENT)))
            .andExpect(status().isOk)

        mockMvc.post(controllerPath + "report", toJson(ReportContentRequest(1, "5673a717-9083-4150-8b7e-c3fb25675e3a", now(), ReportReason.INAPPROPRIATE_CONTENT)))
            .andExpect(status().isOk)

        mockMvc.post(controllerPath + "report", toJson(ReportContentRequest(1, "90d36b5f-e060-4f67-a4a2-c6d06ee76b04", now(), ReportReason.INAPPROPRIATE_CONTENT)))
            .andExpect(status().isOk)

        val reportsString = mockMvc.post(controllerPath + "1/reports", toJson(DEFAULT_LISTING_PARAMS))
            .andExpect(status().isOk)
            .andReturn().response.contentAsString

        val reports = fromJson<ServiceListingResponse<ReportData>>(reportsString).data
        assertEquals(3, reports.filter { it.reason == ReportReason.INAPPROPRIATE_CONTENT }.size)
    }

}