package ge.wanderer.core.model.content.status

import ge.wanderer.common.now
import ge.wanderer.core.jambura
import ge.wanderer.core.model.content.status.ContentStatusType.NOT_RELEVANT
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals

class NotRelevantTest {

    @Test
    fun notRelevantStatusBehavesCorrectly() {
        val statusCreateTime = now()
        val irrelevantStatus = NotRelevant(statusCreateTime)

        assertEquals(NOT_RELEVANT, irrelevantStatus.statusType())
        assertEquals(statusCreateTime, irrelevantStatus.createdAt())
        assertThrows<IllegalStateException> ("Cant remove irrelevant content") { irrelevantStatus.remove(now(), jambura()) }
        assertThrows<IllegalStateException> ("Cant activate irrelevant content") { irrelevantStatus.markIrrelevant(now()) }
        assertThrows<IllegalStateException> ("Already marked as irrelevant") { irrelevantStatus.activate(now(), jambura()) }
    }

}