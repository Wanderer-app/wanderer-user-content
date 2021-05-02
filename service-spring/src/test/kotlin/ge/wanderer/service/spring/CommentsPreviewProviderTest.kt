package ge.wanderer.service.spring

import ge.wanderer.common.enums.UserContentType.*
import ge.wanderer.common.now
import ge.wanderer.core.model.content.CommentableContent
import ge.wanderer.service.spring.test_support.*
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals

class CommentsPreviewProviderTest {

    private val provider = CommentPreviewProvider(mapOf(
        Pair(PIN, 3),
        Pair(POST, 5),
        Pair(POLL, 2)
    ))

    private val comment1 = createNewComment(1, now(), "aaa", jambura())
    private val comment2 = createNewComment(2, now(), "aaa", jangula())
    private val comment3 = createNewComment(3, now(), "aaa", patata())
    private val comment4 = createNewComment(4, now(), "aaa", vipiSoxumski())
    private val comment5 = createNewComment(5, now(), "aaa", kalduna())
    private val comments = listOf(comment1, comment2, comment3, comment4, comment5)

    private val content = mockk<CommentableContent> {
        every { comments() } returns comments
    }

    @Test
    fun returnsPreviewWithCorrectSize() {
        every { content.contentType() } returns PIN
        assertEquals(3, provider.getPreviewFor(content, DEFAULT_LOGGED_IN_USER).size)

        every { content.contentType() } returns POST
        assertEquals(5, provider.getPreviewFor(content, DEFAULT_LOGGED_IN_USER).size)

        every { content.contentType() } returns POLL
        assertEquals(2, provider.getPreviewFor(content, DEFAULT_LOGGED_IN_USER).size)
    }

    @Test
    fun throwsExceptionIfContentTypeNotFoundInPropertiesMap() {
        every { content.contentType() } returns COMMENT
        val ex = assertThrows<IllegalStateException> { provider.getPreviewFor(content, DEFAULT_LOGGED_IN_USER).size }
        assertEquals("Comments preview not available for COMMENT", ex.message!!)
    }

}