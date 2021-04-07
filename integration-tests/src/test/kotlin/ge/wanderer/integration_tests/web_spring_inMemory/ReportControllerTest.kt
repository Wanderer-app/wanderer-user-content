package ge.wanderer.integration_tests.web_spring_inMemory

import ge.wanderer.common.functions.toJson
import ge.wanderer.integration_tests.DEFAULT_LISTING_PARAMS
import ge.wanderer.integration_tests.SpringWebAppWithInMemoryPersistence
import ge.wanderer.integration_tests.post
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestPropertySource
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

@SpringBootTest(classes = [SpringWebAppWithInMemoryPersistence::class], webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@TestPropertySource("classpath:service-spring-test.properties")
class ReportControllerTest(
    @Autowired private val mockMvc: MockMvc
) {
    private val controllerPath = "/api/reports/"

    @Test
    fun listsButRepositoryIsEmpty() {
        mockMvc.post(controllerPath + "list", toJson(DEFAULT_LISTING_PARAMS))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.isSuccessful").value(true))
            .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Reports fetched!"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.resultSize").value(0))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data").isEmpty)
    }

}