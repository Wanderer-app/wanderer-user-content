package ge.wanderer.integration_tests.spring_inMemory

import ge.wanderer.common.enums.UserContentType.POLL
import ge.wanderer.common.enums.UserContentType.POST
import ge.wanderer.integration_tests.SpringServiceWithInMemoryPersistenceApp
import ge.wanderer.service.protocol.interfaces.DiscussionService
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestPropertySource
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@SpringBootTest(classes = [SpringServiceWithInMemoryPersistenceApp::class])
@TestPropertySource("classpath:service-spring-test.properties")
class DiscussionTest(
    @Autowired private val discussionService: DiscussionService
) {
    @Test
    fun listsDiscussionsForRoute() {
        val response = discussionService.getDiscussionForRoute("123", DEFAULT_LISTING_PARAMS)
        assertTrue(response.isSuccessful)
        assertEquals(4, response.resultSize)
        assertTrue(response.data.all { it.type == POLL || it.type == POST })
        assertTrue(response.data.all { it.routeCode == "123" })

        val emptyResponse = discussionService.getDiscussionForRoute("55555", DEFAULT_LISTING_PARAMS)
        assertTrue(emptyResponse.isSuccessful)
        assertEquals(0, emptyResponse.resultSize)
        assertTrue(emptyResponse.data.isEmpty())
    }
}