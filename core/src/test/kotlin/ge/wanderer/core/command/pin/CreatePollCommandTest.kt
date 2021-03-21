package ge.wanderer.core.command.pin

import ge.wanderer.common.now
import ge.wanderer.core.command.discussion.CreatePollCommand
import ge.wanderer.core.getResourceFile
import ge.wanderer.core.kalduna
import ge.wanderer.core.patata
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.lang.IllegalStateException
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class CreatePollCommandTest {

    @Test
    fun failsIfCreatorIsNotAdmin() {
        val exception = assertThrows<IllegalStateException> {
            CreatePollCommand(now(), kalduna(), "some text", "123", listOf("a1", "a2")).execute()
        }
        assertEquals("Only admin can create polls", exception.message!!)
    }

    @Test
    fun failsIfNoAnswersProvided() {
        val exception = assertThrows<IllegalStateException> {
            CreatePollCommand(now(), kalduna(), "some text", "123", listOf()).execute()
        }
        assertEquals("Answers must be provided for a poll!", exception.message!!)
    }

    @Test
    fun correctlyCreatesPoll() {
        val answers = listOf("Gta", "Rdr")
        val result = CreatePollCommand(
            now(),
            patata(),
            "What is the best video game?",
            "123",
            answers
        ).execute()

        assertTrue(result.isSuccessful)
        assertEquals("Poll created!", result.message)

        val poll = result.returnedModel
        assertEquals("123", poll.routeCode())
        assertEquals(2, poll.answersData().size)
        assertEquals(expectedPollJson(), poll.content())
        assertTrue(poll.attachedFiles().isEmpty())
        assertTrue(poll.isActive())
    }

    private fun expectedPollJson(): String =
         this.getResourceFile("bestVideoGamePoll2Answers0Answerers.json")
             .readText()
             .replace("\"answerId\":1", "\"answerId\":0")
             .replace("\"answerId\":2", "\"answerId\":0")

}