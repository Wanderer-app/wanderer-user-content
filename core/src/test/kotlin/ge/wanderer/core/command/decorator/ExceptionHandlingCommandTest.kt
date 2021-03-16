package ge.wanderer.core.command.decorator

import ge.wanderer.core.command.Command
import ge.wanderer.core.model.map.IPin
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ExceptionHandlingCommandTest {
    @Test
    fun correctlyHandlesIllegalStateException() {
        val pin = mockk<IPin>()
        val pinCommand = mockk<Command<IPin>>() {
            every { execute() } throws IllegalStateException("Error on execute")
        }

        val result = ExceptionHandlingCommand<IPin>(pinCommand, pin)
            .execute()

        assertFalse(result.isSuccessful)
        assertEquals("Error on execute", result.message)
    }

    @Test
    fun correctlyHandlesIllegalStateExceptionWithNoMessage() {
        val loan = mockk<IPin>()
        val command = mockk<Command<IPin>>() {
            every { execute() } throws IllegalStateException()
        }

        val result = ExceptionHandlingCommand<IPin>(command, loan)
            .execute()

        assertFalse(result.isSuccessful)
        assertTrue(result.message.startsWith("Exception occured: java.lang.IllegalStateException"))
    }
}