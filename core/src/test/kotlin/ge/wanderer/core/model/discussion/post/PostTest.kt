package ge.wanderer.core.model.discussion.post

import ge.wanderer.common.now
import ge.wanderer.core.model.comment.IComment
import ge.wanderer.core.model.createDownVote
import ge.wanderer.core.model.createNewPostWithoutFiles
import ge.wanderer.core.model.createUpVote
import ge.wanderer.core.model.discussion.DiscussionElementType.*
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class PostTest {

    @Test
    fun isOfCorrectType() {
        val post = createNewPostWithoutFiles(1L, mockk(), "aaa", now())
        assertEquals(POST, post.type())
    }

    @Test
    fun canCorrectlyBeRated() {
        val post = createNewPostWithoutFiles(1L, mockk(), "aaa", now())

        post.giveVote(createUpVote(1, mockk(), now(), 1))
        assertEquals(1, post.rating())

        post.giveVote(createDownVote(2, mockk(), now(), 1))
        assertEquals(0, post.rating())

        val bigDownVote = createDownVote(3, mockk(), now(), 3)
        post.giveVote(bigDownVote)
        assertEquals(-3, post.rating())

        bigDownVote.remove(now())
        assertEquals(0, post.rating())
    }

    @Test
    fun canCorrectlyBeCommented() {
        val post = createNewPostWithoutFiles(1L, mockk(), "aaa", now())

        post.addComment(mockk{ every { isActive() } returns true })
        assertEquals(1, post.comments().size)

        post.addComment(mockk{ every { isActive() } returns true })
        assertEquals(2, post.comments().size)

        post.addComment(mockk{ every { isActive() } returns false })
        assertEquals(2, post.comments().size)
    }

    @Test
    fun canCorrectlyBeRemovedAndActivated() {
        val post = createNewPostWithoutFiles(1L, mockk(), "aaa", now())
        assertTrue(post.isActive())

        post.remove(now())
        assertTrue(post.isRemoved())
        assertFalse(post.isActive())

        post.activate(now())
        assertTrue(post.isActive())
        assertFalse(post.isRemoved())
    }

    @Test
    fun correctlyReturnsContent() {
        val post = createNewPostWithoutFiles(1L, mockk(), "Some text", now())
        assertEquals("Some text", post.content())
    }
}