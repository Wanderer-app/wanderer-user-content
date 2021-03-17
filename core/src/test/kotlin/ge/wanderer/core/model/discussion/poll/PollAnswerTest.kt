package ge.wanderer.core.model.discussion.poll

import ge.wanderer.common.amount
import ge.wanderer.common.now
import ge.wanderer.core.model.*
import io.mockk.mockk
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class PollAnswerTest {

    @Test
    fun returnsCorrectData() {
        val answer = pollAnswer(1, "GTO", now(), mutableSetOf(jambura(), patata(), jangula()), mockk())
        val data = answer.data(9)

        assertEquals(1, data.answerId)
        assertEquals("GTO", data.title)
        assertEquals(amount(33.33), data.percentage)
        assertTrue(data.answererIds.containsAll(listOf(1L, 2L, 3L)))
    }

    @Test
    fun correctlyCalculatesPercentageOfItsAnswerers() {
        val answer = pollAnswer(1, "GTO", now(), mutableSetOf(jambura(), patata(), jangula()), mockk())

        assertEquals(amount(30), answer.percentageOf(10))
        assertEquals(amount(33.33), answer.percentageOf(9))
        assertEquals(amount(3), answer.percentageOf(100))
        assertEquals(amount(10), answer.percentageOf(30))
        assertEquals(amount(100), answer.percentageOf(3))
        assertEquals(amount(300), answer.percentageOf(1))
    }

    @Test
    fun isCorrectlySelectedByUser() {
        val answer = pollAnswer(1, "GTO", now(), mutableSetOf(jambura(), patata()), mockk())
        assertEquals(2, answer.numberOfAnswerers())

        answer.selectBy(jangula())
        assertEquals(3, answer.numberOfAnswerers())

        answer.selectBy(jangula())
        assertEquals(2, answer.numberOfAnswerers())

        answer.selectBy(jambura())
        assertEquals(1, answer.numberOfAnswerers())

        answer.selectBy(jambura())
        assertEquals(2, answer.numberOfAnswerers())
    }

    @Test
    fun canCorrectlyBeRemovedAndActivated() {
        val poll = pollAnswer(1, "GTO", now(), mutableSetOf(jambura(), patata()), mockk())
        assertTrue(poll.isActive())

        poll.remove(now(), jambura())
        assertTrue(poll.isRemoved())
        assertFalse(poll.isActive())

        poll.activate(now(), jambura())
        assertTrue(poll.isActive())
        assertFalse(poll.isRemoved())
    }

    private fun PollAnswer.percentageOf(totalAnswers: Int): BigDecimal = this.data(totalAnswers).percentage
}