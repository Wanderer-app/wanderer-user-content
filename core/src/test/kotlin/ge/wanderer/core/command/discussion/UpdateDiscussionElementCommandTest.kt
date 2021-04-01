package ge.wanderer.core.command.discussion

import ge.wanderer.common.now
import ge.wanderer.core.*
import ge.wanderer.core.model.UpdateDiscussionElementData
import io.mockk.mockk
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class UpdateDiscussionElementCommandTest {

    @Test
    fun failsIfUpdaterIsNotAuthor() {
        val post = createNewPostWithoutFiles(1, jambura(), "Some text", now())

        val exception = assertThrows<IllegalStateException> {
            UpdateDiscussionElementCommand(post, UpdateDiscussionElementData("Updated text", mutableListOf()), patata()).execute()
        }
        assertEquals("You can't update this element", exception.message!!)
    }

    @Test
    fun correctlyUpdatesPosts() {
        val post = createNewPostWithoutFiles(1, jambura(), "Some text", now())

        val result = UpdateDiscussionElementCommand(
            post,
            UpdateDiscussionElementData("Updated text", mutableListOf(mockk(), mockk())),
            jambura()
        ).execute()

        assertTrue(result.isSuccessful)
        assertEquals("POST updated", result.message)
        assertEquals(2, result.returnedModel.attachedFiles().size)
        assertEquals("Updated text", result.returnedModel.content())
    }

    @Test
    fun correctlyUpdatesPolls() {
        val poll = createPoll(1, jambura(), now(), "123", "What is best video game?", mutableSetOf())
        poll.addAnswer(1, "Gta")
        poll.addAnswer(2, "Rdr")

        val result = UpdateDiscussionElementCommand(poll, UpdateDiscussionElementData("What is the best video game?", mutableListOf()), jambura()).execute()
        assertTrue(result.isSuccessful)
        assertEquals("POLL updated", result.message)
        assertEquals(this.getResourceFile("bestVideoGamePoll2Answers0Answerers.json").readText(), result.returnedModel.content())

    }
}