package ge.wanderer.integration_tests.web_spring_inMemory

import ge.wanderer.common.functions.fromJson
import ge.wanderer.common.functions.toJson
import ge.wanderer.integration_tests.DEFAULT_LISTING_PARAMS
import ge.wanderer.integration_tests.SpringWebAppWithInMemoryPersistence
import ge.wanderer.integration_tests.post
import ge.wanderer.service.protocol.data.DiscussionElementData
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
class DiscussionControllerTest(
    @Autowired private val mockMvc: MockMvc
) {

    private val controllerPath = "/api/discussion/"

    @Test
    fun listsDiscussionsForRoute() {
        val routeCode = "123"
        val responseString = mockMvc.post(controllerPath + "for-route/$routeCode", toJson(DEFAULT_LISTING_PARAMS))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.message").value("Discussions fetched"))
            .andReturn().response.contentAsString

        val discussion = fromJson<ServiceListingResponse<DiscussionElementData>>(responseString).data
        assertTrue(discussion.all { it.routeCode == routeCode })
    }
}