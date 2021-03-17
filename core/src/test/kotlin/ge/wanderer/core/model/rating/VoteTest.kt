package ge.wanderer.core.model.rating

import ge.wanderer.common.now
import ge.wanderer.core.model.createDownVote
import ge.wanderer.core.model.createUpVote
import ge.wanderer.core.model.jambura
import ge.wanderer.core.model.patata
import io.mockk.mockk
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.lang.IllegalStateException
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class VoteTest {

    @Test
    fun correctlyGivesValueBasedOnItsType() {
        val upVote = createUpVote(1L, mockk(), now(),  10)
        assertEquals(10, upVote.weight())

        val downVote = createDownVote(2L, mockk(), now(),  10)
        assertEquals(-10, downVote.weight())
    }

    @Test
    fun canOnlyBeRemovedByItsCreator() {
        val vote = createUpVote(1L, jambura(), now(),  10)

        val exception = assertThrows<IllegalStateException> { vote.remove(now(), patata()) }
        assertEquals("Vote can only be removed by it's creator", exception.message!!)

        vote.remove(now(), jambura())
        assertTrue(vote.isRemoved())
    }
}