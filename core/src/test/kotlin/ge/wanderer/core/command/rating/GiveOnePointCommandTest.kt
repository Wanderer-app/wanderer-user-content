package ge.wanderer.core.command.rating

import ge.wanderer.common.now
import ge.wanderer.core.integration.user.UserService
import ge.wanderer.core.model.*
import ge.wanderer.core.model.rating.VoteType
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class GiveOnePointCommandTest {

    @Test
    fun correctlyGivesVotes() {
        val post = createNewPostWithoutFiles(1, jambura(), "aqa mshvidobaa", now())
        val userService: UserService = mockk {
            every { usersContentWasRated(post, any()) } returns Unit
        }

        val result = GiveOnePointCommand(VoteType.UP, jangula(), now(), post, userService)
            .execute()

        assertTrue(result.isSuccessful)
        assertEquals("Vote added", result.message)
        assertEquals(1, result.returnedModel.rating())
        verify(exactly = 1) { userService.usersContentWasRated(post, any()) }
    }

    @Test
    fun failsIfUserVotesForOwnContent() {
        val post = createNewPostWithoutFiles(1, jambura(), "aqa mshvidobaa", now())

        val exception = assertThrows<IllegalStateException> {
            GiveOnePointCommand(VoteType.UP, jambura(), now(), post, mockk()).execute()
        }
        assertEquals("Cant vote for your own content!", exception.message)
    }

    @Test
    fun usersPreviousVotesAreRemoved() {
        val post = createNewPostWithoutFiles(1, jambura(), "aqa mshvidobaa", now())
        val userService = mockk<UserService> {
            every { usersContentWasRated(post, any()) } returns Unit
        }

        GiveOnePointCommand(VoteType.UP, jangula(), now(), post, userService).execute()
        assertEquals(1, post.rating())

        GiveOnePointCommand(VoteType.DOWN, jangula(), now(), post, userService).execute()
        assertEquals(-1, post.rating())

        GiveOnePointCommand(VoteType.DOWN, jangula(), now(), post, userService).execute()
        assertEquals(-1, post.rating())
    }
}