package ge.wanderer.core.command.discussion

import ge.wanderer.common.now
import ge.wanderer.core.createPoll
import ge.wanderer.core.integration.user.UserService
import ge.wanderer.core.jambura
import ge.wanderer.core.model.content.status.Active
import ge.wanderer.core.model.discussion.poll.PollAnswer
import ge.wanderer.core.patata
import ge.wanderer.core.vipiSoxumski
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class RemovePollAnswerCommandTest {

    @Test
    fun correctlyRemovesAnswer() {
        val answer1 = PollAnswer(1, "answer 1", now(), jambura(), Active(now(), jambura()), mutableSetOf())
        val answer2 = PollAnswer(2, "answer 2", now(), jambura(), Active(now(), jambura()), mutableSetOf())
        val answer3 = PollAnswer(3, "answer 3", now(), jambura(), Active(now(), jambura()), mutableSetOf())

        val poll = createPoll(1, jambura(), now(), "123", "aaa", mutableSetOf(answer1, answer2, answer3))
        assertEquals(3, poll.answers().size)

        val result = RemovePollAnswerCommand(poll, jambura(), 3, now(), mockk()).execute()
        assertTrue(result.isSuccessful)
        assertEquals("Answer Removed!", result.message)
        assertEquals(2, result.returnedModel.answers().size)

        assertTrue(answer3.isRemoved())
    }

    @Test
    fun failsIfRemoverIsNotAdmin() {
        val answer1 = PollAnswer(1, "answer 1", now(), jambura(), Active(now(), jambura()), mutableSetOf())
        val answer2 = PollAnswer(2, "answer 2", now(), jambura(), Active(now(), jambura()), mutableSetOf())
        val answer3 = PollAnswer(3, "answer 3", now(), jambura(), Active(now(), jambura()), mutableSetOf())
        val poll = createPoll(1, jambura(), now(), "123", "aaa", mutableSetOf(answer1, answer2, answer3))

        val exception = assertThrows<IllegalStateException> {
            RemovePollAnswerCommand(poll, vipiSoxumski(), 3, now(), mockk()).execute()
        }
        assertEquals("You dont have rights to remove this answer", exception.message!!)
    }

    @Test
    fun notifiesIfAnswerIsRemovedByOtherAdmin() {
        val answer1 = PollAnswer(1, "answer 1", now(), jambura(), Active(now(), jambura()), mutableSetOf())
        val answer2 = PollAnswer(2, "answer 2", now(), jambura(), Active(now(), jambura()), mutableSetOf())
        val answer3 = PollAnswer(3, "answer 3", now(), jambura(), Active(now(), jambura()), mutableSetOf())
        val poll = createPoll(1, jambura(), now(), "123", "aaa", mutableSetOf(answer1, answer2, answer3))
        val userService = mockk<UserService> { every { notifyContentStatusChange(any()) } returns Unit }

        val result = RemovePollAnswerCommand(poll, patata(), 3, now(), userService).execute()
        assertTrue(result.isSuccessful)
        assertEquals("Answer Removed!", result.message)
        assertEquals(2, result.returnedModel.answers().size)
        verify(exactly = 1) { userService.notifyContentStatusChange(answer3) }
    }
}