package ge.wanderer.core.command.discussion

import ge.wanderer.common.now
import ge.wanderer.core.jambura
import io.mockk.mockk
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class CreatePostCommandTest {

    @Test
    fun correctlyCreatesPosts() {
        val result = CreatePostCommand(
            now(),
            jambura(),
            "123",
            "some text",
            mutableListOf(mockk(), mockk())
        ).execute()

        assertTrue(result.isSuccessful)
        assertEquals("Post created!", result.message)

        val post = result.returnedModel
        assertEquals(jambura(), post.creator())
        assertEquals(2, post.attachedFiles().size)
        assertEquals("123", post.routeCode())
        assertEquals("some text", post.content())
        assertEquals(0, post.rating())
        assertTrue(post.isActive())
    }
}