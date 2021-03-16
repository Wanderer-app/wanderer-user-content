package ge.wanderer.core.model.rating

import ge.wanderer.common.now
import ge.wanderer.core.model.createDownVote
import ge.wanderer.core.model.createUpVote
import io.mockk.mockk
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class VoteTest {

    @Test
    fun correctlyGivesValueBasedOnItsType() {
        val upVote = createUpVote(1L, mockk(), now(),  10)
        assertEquals(10, upVote.weight())

        val downVote = createDownVote(2L, mockk(), now(),  10)
        assertEquals(-10, downVote.weight())
    }
}