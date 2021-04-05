package ge.wanderer.core.model.discussion.poll

import ge.wanderer.common.amount
import ge.wanderer.common.now
import ge.wanderer.core.*
import ge.wanderer.core.model.UpdateDiscussionElementData
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
            pollAnswer(1, "GTA", now(), mutableSetOf(jambura(), patata()), mockk()),
            pollAnswer(1, "RDR", now(), mutableSetOf(jangula()), mockk())
        )
        val poll = Poll(1L, mockk(), now(), Active(now(), jambura()), "123", "Best video game?", answers, mutableListOf())

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
            pollAnswer(1, "GTA", now(), mutableSetOf(jambura(), patata()), mockk()),
            pollAnswer(1, "RDR", now(), mutableSetOf(jangula()), mockk())
        )
        val poll = Poll(1L, mockk(), now(), Active(now(), jambura()), "123", "Best video game?", answers, mutableListOf())
        assertTrue(poll.isActive())

        poll.remove(now(), jambura())
        assertTrue(poll.isRemoved())
        assertFalse(poll.isActive())

        poll.activate(now(), jambura())
        assertTrue(poll.isActive())
        assertFalse(poll.isRemoved())
    }

    @Test
    fun correctlyReturnsItsContentAsJson() {
        val poll = createPoll(1, jambura(), now(), "123", "What is the best video game?", mutableSetOf())

        poll.addAnswer(1, "Gta")
        assertEquals(this.getResourceFile("bestVideoGamePoll1Answer0Answerers.json").readText(), poll.content())

        poll.addAnswer(2, "Rdr")
        assertEquals(this.getResourceFile("bestVideoGamePoll2Answers0Answerers.json").readText(), poll.content())

        poll.selectAnswer(1, jambura())
        assertEquals(this.getResourceFile("bestVideoGamePoll2Answers1Answerer.json").readText(), poll.content())

        poll.selectAnswer(1, patata())
        poll.selectAnswer(2, jangula())
        assertEquals(this.getResourceFile("bestVideoGamePoll2Answers3Answerers.json").readText(), poll.content())

        poll.addAnswer(3, "Fifa")
        assertEquals(this.getResourceFile("bestVideoGamePoll3Answers3Answerers.json").readText(), poll.content())

    }

    @Test
    fun correctlySelectsAnswerByUser() {
        val poll = createPoll(1, jambura(), now(), "123", "What is the best video game?", mutableSetOf())
        val gta = pollAnswer(1, "Gta", now(), mutableSetOf(), jambura())
        val rdr = pollAnswer(2, "Rdr", now(), mutableSetOf(), jambura())

        poll.addAnswer(gta)
        poll.addAnswer(rdr)
        assertEquals(2, poll.answersData().size)

        poll.selectAnswer(1, patata())
        poll.selectAnswer(2, jangula())
        assertTrue(poll.answersData().all { it.percentage == amount(50) })

        assertEquals(1, gta.numberOfAnswerers())
        assertEquals(1, rdr.numberOfAnswerers())

        poll.selectAnswer(2, patata())
        assertEquals(0, gta.numberOfAnswerers())
        assertEquals(2, rdr.numberOfAnswerers())

        poll.selectAnswer(1, jangula())
        assertEquals(1, gta.numberOfAnswerers())
        assertEquals(1, rdr.numberOfAnswerers())

        poll.selectAnswer(2, kalduna())
        assertEquals(2, rdr.numberOfAnswerers())

        poll.selectAnswer(2, kalduna())
        assertEquals(1, rdr.numberOfAnswerers())
    }

    @Test
    fun answersCanCorrectlyBeAdded() {
        val answers = mutableSetOf<IPollAnswer>(
            pollAnswer(1, "GTA", now(), mutableSetOf(jambura(), patata()), mockk()),
            pollAnswer(2, "RDR", now(), mutableSetOf(jangula()), mockk())
        )
        val poll = Poll(1L, mockk(), now(), Active(now(), jambura()), "123", "Best video game?", answers, mutableListOf())
        assertEquals(2, poll.answersData().size)

        poll.addAnswer(
            pollAnswer(3, "FIFA", now(), mutableSetOf(), mockk())
        )
        assertEquals(3, poll.answersData().size)

        val ragaca = pollAnswer(4, "ragaca", now(), mutableSetOf(), mockk())
        poll.addAnswer(ragaca)
        assertEquals(4, poll.answersData().size)

        ragaca.remove(now(), jambura())
        assertEquals(3, poll.answersData().size)
    }

    @Test
    fun canBeUpdated() {
        val poll = Poll(1L, mockk(), now(), Active(now(), jambura()), "123", "Best video ggaeme?", mutableSetOf(), mutableListOf())

        poll.update(UpdateDiscussionElementData("Best video game?", listOf()))
        assertTrue(poll.content().startsWith("{\"question\":\"Best video game?\""))
    }
}