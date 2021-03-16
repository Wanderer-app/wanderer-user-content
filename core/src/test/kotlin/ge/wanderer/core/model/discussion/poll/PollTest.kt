package ge.wanderer.core.model.discussion.poll

import ge.wanderer.common.now
import ge.wanderer.core.model.*
import ge.wanderer.core.model.content.status.Active
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class PollTest {

    @Test
    fun canCorrectlyBeCommented() {
        val answers = mutableSetOf<IPollAnswer>(
            pollAnswer(1, "GTA", now(), mutableSetOf(jambura(), patata())),
            pollAnswer(1, "RDR", now(), mutableSetOf(jangula()))
        )
        val poll = Poll(1L, mockk(), now(), Active(now()), "123", "Best video game?", answers, mutableListOf())

        poll.addComment(mockk{ every { isActive() } returns true })
        assertEquals(1, poll.comments().size)

        poll.addComment(mockk{ every { isActive() } returns true })
        assertEquals(2, poll.comments().size)

        poll.addComment(mockk{ every { isActive() } returns false })
        assertEquals(2, poll.comments().size)
    }

    @Test
    fun canCorrectlyBeRemovedAndActivated() {
        val answers = mutableSetOf<IPollAnswer>(
            pollAnswer(1, "GTA", now(), mutableSetOf(jambura(), patata())),
            pollAnswer(1, "RDR", now(), mutableSetOf(jangula()))
        )
        val poll = Poll(1L, mockk(), now(), Active(now()), "123", "Best video game?", answers, mutableListOf())
        assertTrue(poll.isActive())

        poll.remove(now())
        assertTrue(poll.isRemoved())
        assertFalse(poll.isActive())

        poll.activate(now())
        assertTrue(poll.isActive())
        assertFalse(poll.isRemoved())
    }

    @Test
    fun correctlyReturnsItsContentAsJson() {
        val answers = mutableSetOf<IPollAnswer>(
            pollAnswer(1, "GTA", now(), mutableSetOf(jambura(), patata())),
            pollAnswer(1, "RDR", now(), mutableSetOf(jangula()))
        )
        val pollWith2Answers = Poll(1L, mockk(), now(), Active(now()), "123", "Best video game?", answers, mutableListOf())

        val content = pollWith2Answers.content()
        val expectedJson = this.getResourceFile("pollWith2Answers3Answerers.json").readText()
        assertEquals(expectedJson, content)
    }

    @Test
    fun answersCanCorrectlyBeAdded() {
        val answers = mutableSetOf<IPollAnswer>(
            pollAnswer(1, "GTA", now(), mutableSetOf(jambura(), patata())),
            pollAnswer(2, "RDR", now(), mutableSetOf(jangula()))
        )
        val poll = Poll(1L, mockk(), now(), Active(now()), "123", "Best video game?", answers, mutableListOf())
        assertEquals(2, poll.answersData().size)

        poll.addAnswer(
            pollAnswer(3, "FIFA", now(), mutableSetOf())
        )
        assertEquals(3, poll.answersData().size)

        val ragaca = pollAnswer(4, "ragaca", now(), mutableSetOf())
        poll.addAnswer(ragaca)
        assertEquals(4, poll.answersData().size)

        ragaca.remove(now())
        assertEquals(3, poll.answersData().size)
    }

    @Test
    fun canBeUpdated() {
        val poll = Poll(1L, mockk(), now(), Active(now()), "123", "Best video ggaeme?", mutableSetOf(), mutableListOf())

        val updated = poll.update(UpdateDiscussionElementData("Best video game?", listOf()))
        assertTrue(updated.content().startsWith("{\"question\":\"Best video game?\""))
    }
}