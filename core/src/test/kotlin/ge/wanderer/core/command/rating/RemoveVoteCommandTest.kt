package ge.wanderer.core.command.rating

import ge.wanderer.common.now
import ge.wanderer.core.model.*
import io.mockk.mockk
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class RemoveVoteCommandTest {

    @Test
    fun correctlyRemovesVoteFromContent() {
        val pin = createTipPin(1, jambura(), now(), mockk(), "123", "A bear is here")
        val voteByJangula = createUpVote(1, jangula(), now(), 1)
        val voteByKalduna = createUpVote(2, kalduna(), now(), 2)

        pin.giveVote(voteByJangula)
        pin.giveVote(voteByKalduna)
        assertEquals(3, pin.rating())

        var result = RemoveVoteCommand(jangula(), now(), pin).execute()
        assertTrue(result.isSuccessful)
        assertEquals("Vote Removed", result.message)
        assertEquals(2, pin.rating())
        assertTrue(voteByJangula.isRemoved())

        result = RemoveVoteCommand(kalduna(), now(), pin).execute()
        assertTrue(result.isSuccessful)
        assertEquals("Vote Removed", result.message)
        assertEquals(0, pin.rating())
        assertTrue(voteByKalduna.isRemoved())
    }
}