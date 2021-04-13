package ge.wanderer.persistence.inMemory.repository

import ge.wanderer.common.constants.TRANSIENT_ID
import ge.wanderer.common.now
import ge.wanderer.persistence.inMemory.WandererInMemoryPersistenceApplication
import ge.wanderer.persistence.inMemory.support.DEFAULT_LISTING_PARAMS
import ge.wanderer.persistence.inMemory.support.createNewComment
import ge.wanderer.persistence.inMemory.support.jambura
import io.mockk.mockk
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@SpringBootTest(classes = [WandererInMemoryPersistenceApplication::class])
class CommentRepositoryImplTest(
    @Autowired private val commentRepositoryImpl: CommentRepositoryImpl
) {

    @Test
    fun listsComments() {
        val comments = commentRepositoryImpl.list(DEFAULT_LISTING_PARAMS)
        assertTrue(comments.isNotEmpty())
    }

    @Test
    fun persistsComment() {
        val comment = createNewComment(TRANSIENT_ID, now(), "aaa", jambura())
        val newCommentId = commentRepositoryImpl.persist(comment).id()

        val persistedComment = commentRepositoryImpl.findById(newCommentId)
        assertEquals(jambura(), persistedComment.creator())
        assertEquals("aaa", persistedComment.text())
        assertEquals(newCommentId, persistedComment.id())
    }

    @Test
    fun deletesComment() {
        commentRepositoryImpl.delete(1)
        assertThrows<IllegalStateException> { commentRepositoryImpl.findById(1) }
    }
}