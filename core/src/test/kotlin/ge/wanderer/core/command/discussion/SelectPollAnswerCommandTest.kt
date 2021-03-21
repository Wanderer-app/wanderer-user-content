package ge.wanderer.core.command.discussion

import ge.wanderer.common.now
import ge.wanderer.core.addAnswer
import ge.wanderer.core.createPoll
import ge.wanderer.core.jambura
import org.junit.jupiter.api.Test
import kotlin.test.assertTrue

class SelectPollAnswerCommandTest {

    @Test
    fun correctlySelectsAnAnswerByUser() {
        val poll = createPoll(1, jambura(), now(), "123", "Some question", mutableSetOf())
        poll.addAnswer(1, "Answer 1")
        poll.addAnswer(2, "Answer 2")

        assertTrue(poll.answersData().first().answererIds.isEmpty())
        assertTrue(poll.answersData().last().answererIds.isEmpty())

        var result = SelectPollAnswerCommand(poll, 1, jambura()).execute()
        assertTrue(result.isSuccessful)
        assertTrue(poll.answersData().first().answererIds.contains(1))

        result = SelectPollAnswerCommand(poll, 2, jambura()).execute()
        assertTrue(result.isSuccessful)
        assertTrue(poll.answersData().first().answererIds.isEmpty())
        assertTrue(poll.answersData().last().answererIds.contains(1))

        result = SelectPollAnswerCommand(poll, 2, jambura()).execute()
        assertTrue(result.isSuccessful)
        assertTrue(poll.answersData().first().answererIds.isEmpty())
        assertTrue(poll.answersData().last().answererIds.isEmpty())
    }
}