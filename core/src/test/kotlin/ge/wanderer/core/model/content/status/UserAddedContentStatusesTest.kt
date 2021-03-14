package ge.wanderer.core.model.content.status

import ge.wanderer.common.now
import ge.wanderer.core.model.content.status.StatusType.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.lang.IllegalStateException
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class UserAddedContentStatusesTest {

    @Test
    fun activeStatusBehavesCorrectly() {
        val activationTime = now()
        val activeStatus = Active(activationTime)

        assertEquals(ACTIVE, activeStatus.statusType())
        assertEquals(activationTime, activeStatus.createdAt())
        assertTrue(activeStatus.remove(now()) is Removed)
        assertTrue(activeStatus.markIrrelevant(now()) is NotRelevant)
        assertThrows<IllegalStateException> ("Content already active") { activeStatus.activate(now()) }
    }

    @Test
    fun removedStatusBehavesCorrectly() {
        val removeTime = now()
        val removedStatus = Removed(removeTime)

        assertEquals(REMOVED, removedStatus.statusType())
        assertEquals(removeTime, removedStatus.createdAt())
        assertTrue(removedStatus.activate(now()) is Active)
        assertThrows<IllegalStateException> ("Content already removed") { removedStatus.remove(now()) }
        assertThrows<IllegalStateException> ("Content already removed") { removedStatus.markIrrelevant(now()) }
    }

    @Test
    fun notRelevantStatusBehavesCorrectly() {
        val statusCreateTime = now()
        val irrelevantStatus = NotRelevant(statusCreateTime)

        assertEquals(NOT_RELEVANT, irrelevantStatus.statusType())
        assertEquals(statusCreateTime, irrelevantStatus.createdAt())
        assertThrows<IllegalStateException> ("Cant remove irrelevant content") { irrelevantStatus.remove(now()) }
        assertThrows<IllegalStateException> ("Cant activate irrelevant content") { irrelevantStatus.markIrrelevant(now()) }
        assertThrows<IllegalStateException> ("Already marked as irrelevant") { irrelevantStatus.activate(now()) }
    }

}