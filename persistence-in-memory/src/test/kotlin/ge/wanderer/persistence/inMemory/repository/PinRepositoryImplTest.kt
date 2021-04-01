package ge.wanderer.persistence.inMemory.repository

import ge.wanderer.common.now
import ge.wanderer.core.integration.user.UserService
import ge.wanderer.core.model.comment.Comment
import ge.wanderer.core.model.content.status.Active
import ge.wanderer.core.repository.TRANSIENT_ID
import ge.wanderer.persistence.inMemory.WandererInMemoryPersistenceApplication
import io.mockk.mockk
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import kotlin.test.assertEquals

@SpringBootTest(classes = [WandererInMemoryPersistenceApplication::class])
class PinRepositoryImplTest(
    @Autowired private val pinRepositoryImpl: PinRepositoryImpl,
    @Autowired private val commentRepositoryImpl: CommentRepositoryImpl,
    @Autowired private val userService: UserService
) {

    @Test
    fun aaa() {
        assertEquals(5, pinRepositoryImpl.list(mockk()).size)
        val pin = pinRepositoryImpl.findById(3)

        val user = userService.findUserById(7)
        pin.addComment(Comment(TRANSIENT_ID, user, now(), "New comment", Active(now(), user)))

        val comments = commentRepositoryImpl.listActiveFor(pin, mockk())
        assertEquals(2, comments.size)
        assertEquals("New comment", comments.last().text())
    }
}