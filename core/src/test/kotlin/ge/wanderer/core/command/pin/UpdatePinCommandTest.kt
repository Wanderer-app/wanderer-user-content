package ge.wanderer.core.command.pin

import ge.wanderer.common.now
import ge.wanderer.core.createTipPin
import ge.wanderer.core.jambura
import ge.wanderer.core.model.map.RouteElementContent
import ge.wanderer.core.patata
import io.mockk.mockk
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class UpdatePinCommandTest {

    @Test
    fun failsIfUpdaterIsNotAuthor() {
        val pin = createTipPin(1, jambura(), now(), mockk(), "123",  "Some text")

        val exception = assertThrows<IllegalStateException> {
            UpdatePinCommand(pin, RouteElementContent("New title", "Updated text", null), patata()).execute()
        }
        assertEquals("You can't update this pin", exception.message!!)
    }

    @Test
    fun updatesCommentCorrectly() {
        val pin = createTipPin(1, jambura(), now(), mockk(), "123",  "Some text")

        val result = UpdatePinCommand(
            pin,
            RouteElementContent("New title", "Updated text", mockk()),
            jambura()
        ).execute()

        assertTrue(result.isSuccessful)
        assertEquals("Pin updated", result.message)

        val newContent = result.returnedModel.content()
        assertEquals("New title", newContent.title)
        assertEquals("Updated text", newContent.text)
        assertNotNull(newContent.attachedFile)
    }
}