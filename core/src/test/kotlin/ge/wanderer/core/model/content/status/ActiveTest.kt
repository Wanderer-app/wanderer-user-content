package ge.wanderer.core.model.content.status

import ge.wanderer.common.now
import ge.wanderer.core.jambura
import ge.wanderer.core.kalduna
import ge.wanderer.core.vipiSoxumski
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.lang.IllegalStateException
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ActiveTest {

    @Test
    fun activeStatusBehavesCorrectly() {
        val activationTime = now()
        val activeStatus = Active(activationTime, jambura())

        assertEquals(ContentStatusType.ACTIVE, activeStatus.statusType())
        assertEquals(activationTime, activeStatus.createdAt())
        assertTrue(activeStatus.remove(now(), jambura()) is Removed)
        assertTrue(activeStatus.markIrrelevant(now()) is NotRelevant)
        assertThrows<IllegalStateException> ("Content already active") { activeStatus.activate(now(), jambura()) }
    }

    @Test
    fun canOnlyBeRemovedByItsCreatorOrAdmin() {
        val activeStatus = Active(now(), vipiSoxumski())

        assertThrows<IllegalStateException> ("You dont have rights to remove this content") { activeStatus.remove(now(), kalduna()) }
        assertTrue(activeStatus.remove(now(), jambura()) is Removed)
        assertTrue(activeStatus.remove(now(), vipiSoxumski()) is Removed)

    }
}