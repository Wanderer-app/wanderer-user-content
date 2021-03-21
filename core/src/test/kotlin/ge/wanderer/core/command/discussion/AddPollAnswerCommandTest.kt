package ge.wanderer.core.command.discussion

import ge.wanderer.common.now
import ge.wanderer.core.createPoll
import ge.wanderer.core.jambura
import ge.wanderer.core.kalduna
import ge.wanderer.core.patata
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.lang.IllegalStateException
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class AddPollAnswerCommandTest {

    @Test
    fun failsWhenNonAdminTriesToExecuteIt() {
        val poll = createPoll(1, jambura(), now(), "123", "Some question", mutableSetOf())

        val exception = assertThrows<IllegalStateException> {
            AddPollAnswerCommand(poll, "Some answer", now(), kalduna()).execute()
        }
        assertEquals("Only administrators can add new answers to polls", exception.message!!)
    }

    @Test
    fun adminCanAddNewAnswers() {
        val poll = createPoll(1, jambura(), now(), "123", "Some question", mutableSetOf())

        var result = AddPollAnswerCommand(poll, "Answer 1", now(), jambura()).execute()
        assertTrue(result.isSuccessful)
        assertEquals(1, result.returnedModel.answersData().size)

        result = AddPollAnswerCommand(poll, "Answer 2", now(), patata()).execute()
        assertTrue(result.isSuccessful)
        val answersData = result.returnedModel.answersData()
        assertEquals(2, answersData.size)

        assertEquals("Answer 1", answersData.first().title)
        assertEquals("Answer 2", answersData.last().title)
    }
}