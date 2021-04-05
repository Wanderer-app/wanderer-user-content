package ge.wanderer.service.spring.configuration

import ge.wanderer.common.enums.ReportReason
import ge.wanderer.common.enums.UserContentType.PIN
import ge.wanderer.common.now
import ge.wanderer.core.model.map.IPin
import ge.wanderer.core.model.report.Report
import ge.wanderer.service.spring.test_support.jambura
import ge.wanderer.service.spring.test_support.jangula
import ge.wanderer.service.spring.test_support.patata
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ReportingConfigurationImplTest {

    @Test
    fun activeContentWithEnoughReportsShouldBeRemoved() {
        val pin = mockk<IPin> {
            every { isActive() } returns true
            every { contentType() } returns PIN
            every { reports() } returns setOf(
                Report(1, jambura(), now(), ReportReason.IRRELEVANT),
                Report(2, patata(), now(), ReportReason.INAPPROPRIATE_CONTENT),
                Report(2, jangula(), now(), ReportReason.OFFENSIVE_CONTENT)
            )
        }
        val config = ReportingConfigurationImpl(
            mapOf(Pair(PIN, 2)),
            mapOf(Pair(PIN, 2)),
            3
        )
        assertTrue(config.shouldBeRemoved(pin))
    }

    @Test
    fun activeContentWithNotEnoughReportsShouldNotBeRemoved() {
        val pin = mockk<IPin> {
            every { isActive() } returns true
            every { contentType() } returns PIN
            every { reports() } returns setOf(
                Report(1, jambura(), now(), ReportReason.IRRELEVANT),
                Report(2, patata(), now(), ReportReason.INAPPROPRIATE_CONTENT),
                Report(2, jangula(), now(), ReportReason.OFFENSIVE_CONTENT)
            )
        }
        val config = ReportingConfigurationImpl(
            mapOf(Pair(PIN, 3)),
            mapOf(Pair(PIN, 3)),
            3
        )
        assertFalse(config.shouldBeRemoved(pin))
    }

    @Test
    fun removedContentShouldNotBeRemoved() {
        val pin = mockk<IPin> {
            every { isActive() } returns false
            every { contentType() } returns PIN
            every { reports() } returns setOf(
                Report(1, jambura(), now(), ReportReason.IRRELEVANT),
                Report(2, patata(), now(), ReportReason.INAPPROPRIATE_CONTENT),
                Report(2, jangula(), now(), ReportReason.OFFENSIVE_CONTENT)
            )
        }
        val config = ReportingConfigurationImpl(
            mapOf(Pair(PIN, 2)),
            mapOf(Pair(PIN, 2)),
            3
        )
        assertFalse(config.shouldBeRemoved(pin))
    }

    @Test
    fun pinWithEnoughIrrelevantReportsShouldBeMarked() {
        val pin = mockk<IPin> {
            every { contentType() } returns PIN
            every { reports() } returns setOf(
                Report(1, jambura(), now(), ReportReason.IRRELEVANT),
                Report(2, patata(), now(), ReportReason.IRRELEVANT))
        }
        val config = ReportingConfigurationImpl(
            mapOf(Pair(PIN, 0)),
            mapOf(Pair(PIN, 0)),
            2
        )
        assertTrue(config.shouldBeMarkedIrrelevant(pin))
    }

    @Test
    fun pinWithNotEnoughIrrelevantReportsShouldNotBeMarked() {
        val pin = mockk<IPin> {
            every { contentType() } returns PIN
            every { reports() } returns setOf(
                Report(1, jambura(), now(), ReportReason.IRRELEVANT)
            )
        }
        val config = ReportingConfigurationImpl(
            mapOf(Pair(PIN, 0)),
            mapOf(Pair(PIN, 0)),
            2
        )
        assertFalse(config.shouldBeMarkedIrrelevant(pin))
    }

    @Test
    fun administrationShouldNotBeNotifiedOfContentWithNotEnoughReports() {
        val pin = mockk<IPin> {
            every { isActive() } returns true
            every { contentType() } returns PIN
            every { reports() } returns setOf(
                Report(1, jambura(), now(), ReportReason.IRRELEVANT),
                Report(2, patata(), now(), ReportReason.INAPPROPRIATE_CONTENT),
                Report(2, jangula(), now(), ReportReason.OFFENSIVE_CONTENT)
            )
        }
        val config = ReportingConfigurationImpl(
            mapOf(Pair(PIN, 4)),
            mapOf(Pair(PIN, 3)),
            3
        )
        assertFalse(config.shouldNotifyAdministration(pin))
    }

    @Test
    fun administrationShouldBeNotifiedOfContentWithEnoughReports() {
        val pin = mockk<IPin> {
            every { isActive() } returns true
            every { contentType() } returns PIN
            every { reports() } returns setOf(
                Report(1, jambura(), now(), ReportReason.IRRELEVANT),
                Report(2, patata(), now(), ReportReason.INAPPROPRIATE_CONTENT),
                Report(2, jangula(), now(), ReportReason.OFFENSIVE_CONTENT)
            )
        }
        val config = ReportingConfigurationImpl(
            mapOf(Pair(PIN, 3)),
            mapOf(Pair(PIN, 3)),
            3
        )
        assertTrue(config.shouldNotifyAdministration(pin))
    }
}