package ge.wanderer.persistence.inMemory.repository

import ge.wanderer.common.constants.TRANSIENT_ID
import ge.wanderer.common.now
import ge.wanderer.persistence.inMemory.WandererInMemoryPersistenceApplication
import ge.wanderer.persistence.inMemory.support.jambura
import ge.wanderer.persistence.inMemory.support.pollWithAnswers
import io.mockk.mockk
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@SpringBootTest(classes = [WandererInMemoryPersistenceApplication::class])
class PollRepositoryImplTest(
    @Autowired private val pollRepositoryImpl: PollRepositoryImpl
) {

    @Test
    fun listsPolls() {
        val polls = pollRepositoryImpl.list(mockk())
        assertEquals(3, polls.size)
    }

    @Test
    fun persistsPoll() {
        val poll = pollWithAnswers(TRANSIENT_ID, jambura(), now(), "123", "aaa", setOf("1", "2"))
        val newPollId = pollRepositoryImpl.persist(poll).id()

        val persistedPoll = pollRepositoryImpl.findById(newPollId)
        assertEquals(jambura(), persistedPoll.creator())
        assertEquals(2, persistedPoll.answersData().size)
        assertEquals(newPollId, persistedPoll.id())
        assertTrue(persistedPoll.answers().none { it.id() == TRANSIENT_ID })
    }

    @Test
    fun deletesPoll() {
        pollRepositoryImpl.delete(1)
        assertThrows<IllegalStateException> { pollRepositoryImpl.findById(1) }
    }
    
}