package ge.wanderer.core.model.discussion

import ge.wanderer.common.now
import ge.wanderer.core.model.createNewPostWithoutFiles
import ge.wanderer.core.model.discussion.DiscussionElementType.*
import io.mockk.mockk
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class PostTest {

    @Test
    fun aaa() {
        val post = createNewPostWithoutFiles(1L, mockk(), "aaa", now())
        assertEquals(POST, post.type())
    }
}