package ge.wanderer.core.command.pin

import ge.wanderer.common.now
import ge.wanderer.core.configuration.ReportingConfiguration
import ge.wanderer.core.createTipPin
import ge.wanderer.core.integration.user.UserService
import ge.wanderer.core.jambura
import ge.wanderer.core.kalduna
import ge.wanderer.core.model.report.Report
import ge.wanderer.core.model.report.ReportReason.*
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.lang.IllegalStateException
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ReportIrrelevantPinCommandTest {

    @Test
    fun correctlyReportsPinAsIrrelevant() {
        val pin = createTipPin(1, jambura(), now(), mockk(), "123", "some text")

        val reportingConfiguration = mockk<ReportingConfiguration> {
            every { shouldBeMarkedIrrelevant(pin) } returns false
        }
        val userService = mockk<UserService> {
            every { notifyContentStatusChange(pin) } returns Unit
        }
        val result = ReportIrrelevantPinCommand(kalduna(), now(), pin, userService, reportingConfiguration).execute()

        assertTrue(result.isSuccessful)
        assertEquals("Content Reported!", result.message)

        val reportedPin = result.returnedModel
        assertEquals(1, reportedPin.reports().size)
        assertEquals(kalduna(), reportedPin.reports().first().creator)
        assertEquals(IRRELEVANT, reportedPin.reports().first().reason)
        assertTrue(reportedPin.isRelevant())
        verify(exactly = 0) { userService.notifyContentStatusChange(reportedPin) }
    }

    @Test
    fun failsIfAlreadyReportedByTheUser() {
        val pin = createTipPin(1, jambura(), now(), mockk(), "123", "some text")
        pin.report(Report(1, kalduna(), now(), INAPPROPRIATE_CONTENT))

        val reportingConfiguration = mockk<ReportingConfiguration> {
            every { shouldBeMarkedIrrelevant(pin) } returns false
        }

        val exception = assertThrows<IllegalStateException> {
            ReportIrrelevantPinCommand(kalduna(), now(), pin, mockk(), reportingConfiguration).execute()
        }

        assertEquals("You already reported this content", exception.message!!)
    }

    @Test
    fun marksPinAsIrrelevantAfterReport() {
        val pin = createTipPin(1, jambura(), now(), mockk(), "123", "some text")

        val reportingConfiguration = mockk<ReportingConfiguration> {
            every { shouldBeMarkedIrrelevant(pin) } returns true
        }
        val userService = mockk<UserService> {
            every { notifyContentStatusChange(pin) } returns Unit
        }

        val result = ReportIrrelevantPinCommand(kalduna(), now(), pin, userService, reportingConfiguration).execute()
        assertTrue(result.isSuccessful)
        assertEquals("Content Reported!", result.message)

        val reportedPin = result.returnedModel
        assertEquals(1, reportedPin.reports().size)
        assertEquals(kalduna(), reportedPin.reports().first().creator)
        assertEquals(IRRELEVANT, reportedPin.reports().first().reason)
        assertFalse(reportedPin.isRelevant())
        verify(exactly = 1) { userService.notifyContentStatusChange(reportedPin) }

    }

    @Test
    fun failsIfErrorHappensOnExecutingMarkCommand() {
        val pin = createTipPin(1, jambura(), now(), mockk(), "123", "some text")
        pin.remove(now(), jambura())

        val reportingConfiguration = mockk<ReportingConfiguration> {
            every { shouldBeMarkedIrrelevant(pin) } returns true
        }
        val userService = mockk<UserService> {
            every { notifyContentStatusChange(pin) } returns Unit
        }

        val exception = assertThrows<IllegalStateException> {
            ReportIrrelevantPinCommand(kalduna(), now(), pin, userService, reportingConfiguration).execute()
        }
        assertEquals("Content already removed", exception.message!!)
    }
}