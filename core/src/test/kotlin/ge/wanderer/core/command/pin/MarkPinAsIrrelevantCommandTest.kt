package ge.wanderer.core.command.pin

import ge.wanderer.common.now
import ge.wanderer.core.createTipPin
import ge.wanderer.core.integration.user.UserService
import ge.wanderer.core.jambura
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class MarkPinAsIrrelevantCommandTest {

    @Test
    fun marksActivePinsAsIrrelevantAndNotifiesUserService() {
        val pin = createTipPin(1, jambura(), now(), mockk(), "123", "Lots of wolfs here")
        val userService: UserService = mockk { every { notifyContentStatusChange(pin) } returns Unit }

        val result = MarkPinAsIrrelevantCommand(pin, now(), userService).execute()
        assertTrue(result.isSuccessful)
        assertEquals("Successfully marked as irrelevant", result.message)
        verify(exactly = 1) { userService.notifyContentStatusChange(pin) }
    }

    @Test
    fun failsIfPinIsRemoved() {
        val pin = createTipPin(1, jambura(), now(), mockk(), "123", "Lots of wolfs here")
        pin.remove(now(), jambura())
        val userService: UserService = mockk { every { notifyContentStatusChange(pin) } returns Unit }

        val exception = assertThrows<IllegalStateException> {
            MarkPinAsIrrelevantCommand(pin, now(), userService).execute()
        }
        assertEquals("Content already removed", exception.message!!)
        verify(exactly = 0) { userService.notifyContentStatusChange(pin) }
    }

    @Test
    fun failsIfPinIsAlreadyIrrelevant() {
        val pin = createTipPin(1, jambura(), now(), mockk(), "123", "Lots of wolfs here")
        pin.markIrrelevant(now())
        val userService: UserService = mockk { every { notifyContentStatusChange(pin) } returns Unit }

        val exception = assertThrows<IllegalStateException> {
            MarkPinAsIrrelevantCommand(pin, now(), userService).execute()
        }
        assertEquals("Already marked as irrelevant", exception.message!!)
        verify(exactly = 0) { userService.notifyContentStatusChange(pin) }
    }
}