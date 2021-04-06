package ge.wanderer.integration_tests.web_spring_inMemory

import ge.wanderer.common.fromJson
import ge.wanderer.common.toJson
import ge.wanderer.integration_tests.DEFAULT_LISTING_PARAMS
import ge.wanderer.integration_tests.SpringWebAppWithInMemoryPersistence
import ge.wanderer.integration_tests.get
import ge.wanderer.integration_tests.post
import ge.wanderer.service.protocol.data.PinData
import ge.wanderer.service.protocol.response.ServiceListingResponse
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestPropertySource
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import kotlin.test.assertTrue

@SpringBootTest(classes = [SpringWebAppWithInMemoryPersistence::class], webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@TestPropertySource("classpath:service-spring-test.properties")
class PinWebTest(
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
    fun listsPinsFprGivenRoute() {
        val responseString = mockMvc.post(controllerPath + "for-route/123", toJson(DEFAULT_LISTING_PARAMS))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.isSuccessful").value(true))
            .andExpect(jsonPath("$.message").value("Pins Fetched!"))
            .andReturn()
            .response.contentAsString

        val responseData: List<PinData> = fromJson<ServiceListingResponse<PinData>>(responseString).data
        assertTrue(responseData.isNotEmpty())
        assertTrue(responseData.all { it.routeCode == "123" })
    }

}