package ge.wanderer.core.model.discussion.post

import ge.wanderer.common.enums.UserContentType
import ge.wanderer.common.now
import ge.wanderer.core.*
import ge.wanderer.core.model.*
import ge.wanderer.core.model.report.Report
import ge.wanderer.core.model.report.ReportReason.*
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.lang.IllegalStateException
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class PostTest {

    @Test
    fun isOfCorrectType() {
        val post = createNewPostWithoutFiles(1L, mockk(), "aaa", now())
        assertEquals(UserContentType.POST, post.contentType())
    }

    @Test
    fun canCorrectlyBeRated() {
        val post = createNewPostWithoutFiles(1L, mockk(), "aaa", now())

        post.giveVote(createUpVote(1, mockk(), now(), 1))
        assertEquals(1, post.rating())

        post.giveVote(createDownVote(2, mockk(), now(), 1))
        assertEquals(0, post.rating())

        val bigDownVote = createDownVote(3, jambura(), now(), 3)
        post.giveVote(bigDownVote)
        assertEquals(-3, post.rating())

        bigDownVote.remove(now(), jambura())
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

        post.remove(now(), jambura())
        assertTrue(post.isRemoved())
        assertFalse(post.isActive())

        post.activate(now(), jambura())
        assertTrue(post.isActive())
        assertFalse(post.isRemoved())
    }

    @Test
    fun correctlyReturnsContent() {
        val post = createNewPostWithoutFiles(1L, mockk(), "Some text", now())
        assertEquals("Some text", post.content())
    }

    @Test
    fun canBeUpdated() {
        val post = createNewPostWithoutFiles(1, mockk(), "Hellooo", now())

        val updateData = UpdateDiscussionElementData("Hello", listOf(mockk(), mockk()))
        val updated = post.update(updateData)

        assertEquals("Hello", updated.content())
        assertEquals(2, updated.attachedFiles().size)
    }

    @Test
    fun canCorrectlyBeReported() {
        val post = createNewPostWithoutFiles(1, mockk(), "Hellooo", now())

        post.report(Report(1, kalduna(), now(), INAPPROPRIATE_CONTENT))
        assertEquals(1, post.reports().size)

        post.report(Report(2, jangula(), now(), OFFENSIVE_CONTENT))
        assertEquals(2, post.reports().size)

        post.report(Report(3, vipiSoxumski(), now(), OFFENSIVE_CONTENT))
        assertEquals(3, post.reports().size)
    }

    @Test
    fun cantBeReportedMoreThanOnceBySameUser() {
        val post = createNewPostWithoutFiles(1, mockk(), "Hellooo", now())
        post.report(Report(1, kalduna(), now(), INAPPROPRIATE_CONTENT))

        val exception = assertThrows<IllegalStateException> {
            post.report(Report(2, kalduna(), now(), OFFENSIVE_CONTENT))
        }
        assertEquals("You already reported this content", exception.message!!)
    }

    @Test
    fun cantBeReportedAsIrrelevant() {
        val post = createNewPostWithoutFiles(1, mockk(), "Hellooo", now())

        val exception = assertThrows<IllegalStateException> {
            post.report(Report(2, kalduna(), now(), IRRELEVANT))
        }
        assertEquals("Cant report POST with reason IRRELEVANT", exception.message!!)
    }
}