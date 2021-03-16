package ge.wanderer.core.model.map

import ge.wanderer.common.map.LatLng
import ge.wanderer.common.now
import ge.wanderer.core.model.rating.Vote
import ge.wanderer.core.model.rating.VoteType.*
import ge.wanderer.core.model.content.status.Active
import ge.wanderer.core.model.content.status.NotRelevant
import ge.wanderer.core.model.content.status.Removed
import ge.wanderer.core.model.createDownVote
import ge.wanderer.core.model.createNewComment
import ge.wanderer.core.model.createTipPin
import ge.wanderer.core.model.createUpVote
import io.mockk.mockk
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.lang.IllegalStateException
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

        pin.remove(now())
        assertTrue(pin.isRemoved())
        assertThrows<IllegalStateException>("Content already removed") { pin.markIrrelevant(now()) }

        pin.activate(now())
        assertTrue(pin.isActive())
    }

    @Test
    fun canBeMarkedIrrelevant() {
        val pin = createTipPin(1L, mockk(), now(), LatLng(1f, 1f), "1234", "Rest here before going further")

        pin.markIrrelevant(now())
        assertFalse(pin.isRelevant())

        assertThrows<IllegalStateException> { pin.activate(now()) }
        assertThrows<IllegalStateException> { pin.remove(now()) }
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
}