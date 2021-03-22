package ge.wanderer.core.command.content

import ge.wanderer.common.now
import ge.wanderer.core.*
import ge.wanderer.core.configuration.ReportingConfiguration
import ge.wanderer.core.integration.user.UserService
import ge.wanderer.core.model.report.ReportReason.*
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ReportContentCommandTest {

    @Test
    fun correctlyReportsComment() {
        val comment = createNewComment(1, now(), "Inappropriate text", jangula())

        val reportingConfiguration = mockk<ReportingConfiguration> {
            every { shouldBeRemoved(comment) } returns false
            every { shouldNotifyAdministration(comment) } returns false
        }
        val result = ReportContentCommand(kalduna(), now(), INAPPROPRIATE_CONTENT, comment, mockk(), reportingConfiguration).execute()

        assertTrue(result.isSuccessful)
        assertEquals("Content Reported!", result.message)

        val reportedComment = result.returnedModel
        assertEquals(1, reportedComment.reports().size)
        assertEquals(kalduna(), reportedComment.reports().first().creator)
        assertEquals(INAPPROPRIATE_CONTENT, reportedComment.reports().first().reason)
        verify(exactly = 1) { reportingConfiguration.shouldBeRemoved(comment) }
        verify(exactly = 1) { reportingConfiguration.shouldNotifyAdministration(comment) }
    }

    @Test
    fun notifiesAdministrationWhenNecessary() {
        val comment = createNewComment(1, now(), "Inappropriate text", jangula())

        val reportingConfiguration = mockk<ReportingConfiguration> {
            every { shouldBeRemoved(comment) } returns false
            every { shouldNotifyAdministration(comment) } returns true
        }
        val userService = mockk<UserService>() {
            every { notifyAdministrationAboutReport(comment) } returns Unit
        }
        val result = ReportContentCommand(kalduna(), now(), INAPPROPRIATE_CONTENT, comment, userService, reportingConfiguration).execute()

        assertTrue(result.isSuccessful)
        assertEquals("Content Reported!", result.message)
        assertEquals(1, result.returnedModel.reports().size)
        verify(exactly = 1) { userService.notifyAdministrationAboutReport(comment) }
    }

    @Test
    fun removesContentWhenNecessary() {
        val comment = createNewComment(1, now(), "Inappropriate text", jangula())

        val reportingConfiguration = mockk<ReportingConfiguration> {
            every { shouldBeRemoved(comment) } returns true
            every { shouldNotifyAdministration(comment) } returns false
        }
        val userService = mockk<UserService>() {
            every { notifyAdministrationAboutReport(comment) } returns Unit
            every { notifyContentStatusChange(comment) } returns Unit
            every { getAdministrationUser() } returns jambura()
        }
        val result = ReportContentCommand(kalduna(), now(), INAPPROPRIATE_CONTENT, comment, userService, reportingConfiguration).execute()

        assertTrue(result.isSuccessful)
        assertEquals("Content Reported!", result.message)
        assertEquals(1, result.returnedModel.reports().size)
        assertTrue(result.returnedModel.isRemoved())
        verify(exactly = 1) { userService.notifyContentStatusChange(comment) }
    }

    @Test
    fun failsWhenReportReasonIsNotSupported() {
        val comment = createNewComment(1, now(), "Inappropriate text", jangula())

        val exception = assertThrows<IllegalStateException> {
            ReportContentCommand(kalduna(), now(), IRRELEVANT, comment, mockk(), mockk()).execute()
        }
        assertEquals("Cant report COMMENT with reason IRRELEVANT", exception.message!!)
    }

    @Test
    fun correctlyReportsPost() {
        val post = createNewPostWithoutFiles(1, jangula(), "Inappropriate text", now())

        val reportingConfiguration = mockk<ReportingConfiguration> {
            every { shouldBeRemoved(post) } returns false
            every { shouldNotifyAdministration(post) } returns false
        }
        val result = ReportContentCommand(kalduna(), now(), INAPPROPRIATE_CONTENT, post, mockk(), reportingConfiguration).execute()

        assertTrue(result.isSuccessful)
        assertEquals("Content Reported!", result.message)

        val reportedComment = result.returnedModel
        assertEquals(1, reportedComment.reports().size)
        assertEquals(kalduna(), reportedComment.reports().first().creator)
        assertEquals(INAPPROPRIATE_CONTENT, reportedComment.reports().first().reason)
        verify(exactly = 1) { reportingConfiguration.shouldBeRemoved(post) }
        verify(exactly = 1) { reportingConfiguration.shouldNotifyAdministration(post) }
    }

    @Test
    fun correctlyReportsPins() {
        val pin = createTipPin(1, jangula(), now(), mockk(), "123", "Inappropriate text")

        val reportingConfiguration = mockk<ReportingConfiguration> {
            every { shouldBeRemoved(pin) } returns false
            every { shouldNotifyAdministration(pin) } returns false
        }
        val result = ReportContentCommand(kalduna(), now(), INAPPROPRIATE_CONTENT, pin, mockk(), reportingConfiguration).execute()

        assertTrue(result.isSuccessful)
        assertEquals("Content Reported!", result.message)

        val reportedComment = result.returnedModel
        assertEquals(1, reportedComment.reports().size)
        assertEquals(kalduna(), reportedComment.reports().first().creator)
        assertEquals(INAPPROPRIATE_CONTENT, reportedComment.reports().first().reason)
        verify(exactly = 1) { reportingConfiguration.shouldBeRemoved(pin) }
        verify(exactly = 1) { reportingConfiguration.shouldNotifyAdministration(pin) }
    }
}