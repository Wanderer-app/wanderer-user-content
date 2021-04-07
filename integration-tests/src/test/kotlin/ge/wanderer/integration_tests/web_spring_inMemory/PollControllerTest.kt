package ge.wanderer.integration_tests.web_spring_inMemory

import ge.wanderer.integration_tests.SpringWebAppWithInMemoryPersistence
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestPropertySource
import org.springframework.test.web.servlet.MockMvc

@SpringBootTest(classes = [SpringWebAppWithInMemoryPersistence::class], webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@TestPropertySource("classpath:service-spring-test.properties")
class PollControllerTest(
    @Autowired private val mockMvc: MockMvc
) {

    private val controllerPath = "/api/polls/"

    @Test
    fun aaa() {

    }

}