package ge.wanderer.persistence.inMemory.repository

import ge.wanderer.common.now
import ge.wanderer.core.repository.TRANSIENT_ID
import ge.wanderer.persistence.inMemory.WandererInMemoryPersistenceApplication
import ge.wanderer.persistence.inMemory.support.createNewComment
import ge.wanderer.persistence.inMemory.support.jambura
import io.mockk.mockk
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.lang.IllegalStateException
import kotlin.test.assertEquals

@SpringBootTest(classes = [WandererInMemoryPersistenceApplication::class])
class CommentRepositoryImplTest(
    @Autowired private val commentRepositoryImpl: CommentRepositoryImpl
) {

    @Test
    fun listsComments() {
        val comments = commentRepositoryImpl.list(mockk())
        assertEquals(13, comments.size)
    }

    @Test
    fun persistsComment() {
        val comment = createNewComment(TRANSIENT_ID, now(), "aaa", jambura())
        commentRepositoryImpl.persist(comment)

        val persistedComment = commentRepositoryImpl.findById(15)
        assertEquals(jambura(), persistedComment.creator())
        assertEquals("aaa", persistedComment.text())
        assertEquals(14, commentRepositoryImpl.list(mockk()).size)
    }

    @Test
    fun deletesComment() {
        commentRepositoryImpl.delete(1)
        assertThrows<IllegalStateException> { commentRepositoryImpl.findById(1) }
    }
}