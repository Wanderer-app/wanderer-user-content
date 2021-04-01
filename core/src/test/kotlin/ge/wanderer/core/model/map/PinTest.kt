package ge.wanderer.core.model.map

import ge.wanderer.common.map.LatLng
import ge.wanderer.common.now
import ge.wanderer.core.*
import ge.wanderer.core.model.report.Report
import ge.wanderer.core.model.report.ReportReason.*
import io.mockk.mockk
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class  PinTest {

    @Test
    fun canCorrectlyBeRanked() {
        val pin = createTipPin(1L, mockk(), now(), LatLng(1f, 1f), "1234", "Rest here before going further")

        pin.giveVote(createUpVote(1L, mockk(), now(), 1))
        assertEquals(1, pin.rating())

        pin.giveVote(createUpVote(1L, mockk(), now(), 10))
        assertEquals(11, pin.rating())

        pin.giveVote(createDownVote(2L, mockk(), now(), 11))
        assertEquals(0, pin.rating())

        pin.giveVote(createDownVote(2L, mockk(), now(), 10))
        assertEquals(-10, pin.rating())
    }

    @Test
    fun canCorrectlyBeCommented() {
        val pin = createTipPin(1L, mockk(), now(), LatLng(1f, 1f), "1234", "Rest here before going further")

        pin.addComment(createNewComment(1L, now(), "Thanks", mockk()) )
        assertEquals(1, pin.comments().size)

        pin.addComment(createNewComment(1L, now(), "Thanks You!", mockk()) )
        assertEquals(2, pin.comments().size)

    }

    @Test
    fun canCorrectlyBeRemovedAndActivated() {
        val pin = createTipPin(1L, mockk(), now(), LatLng(1f, 1f), "1234", "Rest here before going further")

        pin.remove(now(), jambura())
        assertTrue(pin.isRemoved())
        assertThrows<IllegalStateException>("Content already removed") { pin.markIrrelevant(now()) }

        pin.activate(now(), jambura())
        assertTrue(pin.isActive())
    }

    @Test
    fun canBeMarkedIrrelevant() {
        val pin = createTipPin(1L, mockk(), now(), LatLng(1f, 1f), "1234", "Rest here before going further")

        pin.markIrrelevant(now())
        assertFalse(pin.isRelevant())

        assertThrows<IllegalStateException> { pin.activate(now(), jambura()) }
        assertThrows<IllegalStateException> { pin.remove(now(), jambura()) }
        assertThrows<IllegalStateException> { pin.markIrrelevant(now()) }
    }

    @Test
    fun canBeUpdated() {
        val pin = createTipPin(1L, mockk(), now(), LatLng(1f, 1f), "1234", "Rest here")

        val newContent = RouteElementContent("Resting place", "Rest here before going further", mockk())
        val updated = pin.update(newContent)

        assertEquals("Resting place", updated.content().title)
        assertEquals("Rest here before going further", updated.content().text)
        assertNotNull(updated.content().attachedFile)
    }

    @Test
    fun canCorrectlyBeReported() {
        val pin = createTipPin(1L, mockk(), now(), LatLng(1f, 1f), "1234", "Inappropriate text")

        pin.report(Report(1, kalduna(), now(), INAPPROPRIATE_CONTENT))
        assertEquals(1, pin.reports().size)

        pin.report(Report(2, jangula(), now(), OFFENSIVE_CONTENT))
        assertEquals(2, pin.reports().size)

        pin.report(Report(3, vipiSoxumski(), now(), IRRELEVANT))
        assertEquals(3, pin.reports().size)
    }

    @Test
    fun cantBeReportedMoreThanOnceBySameUser() {
        val pin = createTipPin(1L, mockk(), now(), LatLng(1f, 1f), "1234", "Inappropriate text")
        pin.report(Report(1, kalduna(), now(), INAPPROPRIATE_CONTENT))

        val exception = assertThrows<IllegalStateException> {
            pin.report(Report(2, kalduna(), now(), OFFENSIVE_CONTENT))
        }
        assertEquals("You already reported this content", exception.message!!)
    }
}