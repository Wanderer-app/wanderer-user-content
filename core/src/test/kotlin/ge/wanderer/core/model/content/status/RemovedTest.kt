package ge.wanderer.core.model.content.status

import ge.wanderer.common.now
import ge.wanderer.core.jambura
import ge.wanderer.core.jangula
import ge.wanderer.core.kalduna
import ge.wanderer.core.vipiSoxumski
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.lang.IllegalStateException
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class RemovedTest {

    @Test
    fun removedStatusBehavesCorrectly() {
        val removeTime = now()
        val removedStatus = Removed(removeTime, jambura())

        assertEquals(ContentStatusType.REMOVED, removedStatus.statusType())
        assertEquals(removeTime, removedStatus.createdAt())
        assertTrue(removedStatus.activate(now(), jambura()) is Active)
        assertThrows<IllegalStateException> ("Content already removed") { removedStatus.remove(now(), jambura()) }
        assertThrows<IllegalStateException> ("Content already removed") { removedStatus.markIrrelevant(now()) }
    }

    @Test
    fun whenWasNotRemovedByAdmin_CanOnlyBeActivatedByItsCreator() {
        val removedStatus = Removed(now(), vipiSoxumski())

        assertThrows<IllegalStateException> ("You dont have rights to activate this content") {
            removedStatus.activate(now(), jambura())
        }
        assertThrows<IllegalStateException> ("You dont have rights to activate this content") {
            removedStatus.activate(now(), kalduna())
        }
        assertTrue(removedStatus.activate(now(), vipiSoxumski()) is Active)

    }

    @Test
    fun whenWasRemovedByAdmin_CanOnlyBeActivatedByAdmin() {
        val removedStatus = Removed(now(), jambura())

        assertThrows<IllegalStateException> ("You dont have rights to activate this content") {
            removedStatus.activate(now(), vipiSoxumski())
        }
        assertTrue(removedStatus.activate(now(), jangula()) is Active)
        assertTrue(removedStatus.activate(now(), jambura()) is Active)

    }
}