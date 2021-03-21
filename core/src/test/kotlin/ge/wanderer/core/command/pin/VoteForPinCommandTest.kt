package ge.wanderer.core.command.pin

import ge.wanderer.common.now
import ge.wanderer.core.integration.user.UserService
import ge.wanderer.core.createTipPin
import ge.wanderer.core.jambura
import ge.wanderer.core.jangula
import ge.wanderer.core.kalduna
import ge.wanderer.core.model.rating.VoteType
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class VoteForPinCommandTest {

    @Test
    fun voteWeightDependsOnUser() {
        val pin = createTipPin(1, jambura(), now(), mockk(), "123", "A bear is here")
        val userService = mockk<UserService> {
            every { usersContentWasRated(pin, any()) } returns Unit
        }

        var result = VoteForPinCommand(VoteType.UP, jangula(), now(), pin, userService).execute()
        assertTrue(result.isSuccessful)
        assertEquals("Vote added", result.message)
        verify(exactly = 1) { userService.usersContentWasRated(pin, any()) }
        assertEquals(jangula().pinVoteWeight, result.returnedModel.rating())

        result = VoteForPinCommand(VoteType.DOWN, kalduna(), now(), pin, userService).execute()
        assertTrue(result.isSuccessful)
        assertEquals("Vote added", result.message)
        verify(exactly = 2) { userService.usersContentWasRated(pin, any()) }
        assertEquals(jangula().pinVoteWeight - kalduna().pinVoteWeight, result.returnedModel.rating())
    }
}