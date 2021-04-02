package ge.wanderer.persistence.inMemory.repository

import ge.wanderer.common.enums.UserContentType.*
import ge.wanderer.persistence.inMemory.WandererInMemoryPersistenceApplication
import io.mockk.mockk
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import kotlin.test.assertEquals

@SpringBootTest(classes = [WandererInMemoryPersistenceApplication::class])
class DiscussionRepositoryImplTest(
    @Autowired private val discussionRepositoryImpl: DiscussionRepositoryImpl
) {

    @Test
    fun listsBothPostsAndPollsForRoute() {
        val discussion = discussionRepositoryImpl.listForRoute("123", mockk())

        assertEquals(4, discussion.size)
        assertEquals(2, discussion.count { it.contentType() == POST })
        assertEquals(2, discussion.count { it.contentType() == POLL })
    }
}