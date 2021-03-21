package ge.wanderer.core.model.comment

import ge.wanderer.common.now
import ge.wanderer.common.dateTime
import ge.wanderer.core.*
import ge.wanderer.core.integration.user.User
import ge.wanderer.core.model.*
import ge.wanderer.core.model.report.Report
import ge.wanderer.core.model.report.ReportReason
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.lang.IllegalStateException
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class CommentTest {

    @Test
    fun isCorrectlyRated() {
        val comment = createNewComment(1L, now(), "Some text", mockk())

        comment.giveVote(createUpVote(1L, mockk(), now(), 1))
        assertEquals(1, comment.rating())

        comment.giveVote(createUpVote(2L, mockk(), now(), 2))
        assertEquals(3, comment.rating())

        comment.giveVote(createDownVote(2L, mockk(), now(), 1))
        assertEquals(2, comment.rating())

        comment.giveVote(createDownVote(2L, mockk(), now(), 3))
        assertEquals(-1, comment.rating())
    }

    @Test
    fun isCorrectlyRepliedTo() {
        val comment = createNewComment(1L, now(), "Some text", mockk())
        val reply1 = createNewComment(1L, now().plusHours(1), "Reply 1", mockk())
        val reply2 = createNewComment(1L, now().plusHours(2), "Reply 2", mockk())
        val reply3 = createNewComment(1L, now().plusHours(3), "Reply 3", mockk())
        val replyToReply1 = createNewComment(1L, now().plusHours(3), "Reply to reply 1", mockk())

        assertEquals(0, comment.comments().size)

        comment.addComment(reply1)
        assertEquals(1, comment.comments().size)

        comment.addComment(reply2)
        assertEquals(2, comment.comments().size)

        comment.addComment(reply3)
        assertEquals(3, comment.comments().size)

        reply1.addComment(replyToReply1)
        assertEquals(3, comment.comments().size)
        assertEquals(1, comment.comments()[0].comments().size)

        reply2.remove(now(), mockk { every { isAdmin } returns true})
        assertEquals(2, comment.comments().size)

    }

    @Test
    fun canBeRemovedAndActivated() {
        val banDate = dateTime("2021-03-15T00:00:00")
        val unBanDate = dateTime("2021-03-16T00:00:00")
        val comment = createNewComment(1L, now(), "Inappropriate text", mockk<User>())

        comment.remove(banDate, jambura())
        assertEquals(banDate, comment.statusUpdatedAt())
        assertTrue(comment.isRemoved())

        comment.activate(unBanDate, jambura())
        assertEquals(unBanDate, comment.statusUpdatedAt())
        assertTrue(comment.isActive())
    }

    @Test
    fun canBeUpdated() {
        val comment = createNewComment(1L, now(), "SomeText", mockk())

        val updateData = UpdateCommentData("Some text")
        val updated = comment.update(updateData)

        assertEquals("Some text", updated.text())
    }

    @Test
    fun canCorrectlyBeReported() {
        val comment = createNewComment(1L, now(), "SomeText", mockk())

        comment.report(Report(1, kalduna(), now(), ReportReason.INAPPROPRIATE_CONTENT))
        assertEquals(1, comment.reports().size)

        comment.report(Report(2, jangula(), now(), ReportReason.OFFENSIVE_CONTENT))
        assertEquals(2, comment.reports().size)

        comment.report(Report(3, vipiSoxumski(), now(), ReportReason.OFFENSIVE_CONTENT))
        assertEquals(3, comment.reports().size)
    }

    @Test
    fun cantBeReportedMoreThanOnceBySameUser() {
        val comment = createNewComment(1L, now(), "SomeText", mockk())
        comment.report(Report(1, kalduna(), now(), ReportReason.INAPPROPRIATE_CONTENT))

        val exception = assertThrows<IllegalStateException> {
            comment.report(Report(2, kalduna(), now(), ReportReason.OFFENSIVE_CONTENT))
        }
        assertEquals("You already reported this content", exception.message!!)
    }

    @Test
    fun cantBeReportedAsIrrelevant() {
        val comment = createNewComment(1L, now(), "SomeText", mockk())

        val exception = assertThrows<IllegalStateException> {
            comment.report(Report(2, kalduna(), now(), ReportReason.IRRELEVANT))
        }
        assertEquals("Cant report COMMENT with reason IRRELEVANT", exception.message!!)
    }

}