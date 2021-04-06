package ge.wanderer.integration_tests.web_spring_inMemory

import ge.wanderer.integration_tests.SpringWebAppWithInMemoryPersistence
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.TestPropertySource
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

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
        mockMvc.perform(MockMvcRequestBuilders.get(controllerPath + pinId).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.isSuccessful").value(true))
            .andExpect(jsonPath("$.message").value("Pin fetched!"))
            .andExpect(jsonPath("$.data.id").value(pinId))
    }

}