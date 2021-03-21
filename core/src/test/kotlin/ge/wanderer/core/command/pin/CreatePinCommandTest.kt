package ge.wanderer.core.command.pin

import ge.wanderer.common.enums.UserContentType
import ge.wanderer.common.map.LatLng
import ge.wanderer.common.now
import ge.wanderer.core.jambura
import ge.wanderer.core.model.map.MarkerType
import ge.wanderer.core.model.map.RouteElementContent
import io.mockk.mockk
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class CreatePinCommandTest {

    @Test
    fun correctlyCreatesPin() {
        val result = CreatePinCommand(
            now(),
            jambura(),
            MarkerType.WARNING,
            RouteElementContent("A title", "Some text", mockk()),
            LatLng(1.25F, 5.21F),
            "123"
        ).execute()

        assertTrue(result.isSuccessful)
        assertEquals("Pin Created", result.message)

        val pin = result.returnedModel
        assertEquals(jambura(), pin.creator())
        assertEquals("123", pin.routeCode())
        assertEquals(LatLng(1.25F, 5.21F), pin.location())
        assertEquals(UserContentType.PIN, pin.contentType())
        assertEquals("Some text", pin.content().text)
        assertEquals("A title", pin.content().title)
        assertNotNull(pin.content().attachedFile)
        assertEquals(0, pin.rating())
        assertTrue(pin.isRelevant())
        assertTrue(pin.isActive())
    }
}