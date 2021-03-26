package ge.wanderer.service.spring.impl

import ge.wanderer.common.dateTime
import ge.wanderer.common.map.LatLng
import ge.wanderer.common.now
import ge.wanderer.core.configuration.ReportingConfiguration
import ge.wanderer.core.model.map.IPin
import ge.wanderer.core.model.map.MarkerType.*
import ge.wanderer.core.model.map.RouteElementContent
import ge.wanderer.core.repository.CommentRepository
import ge.wanderer.service.protocol.request.CreatePinRequest
import ge.wanderer.service.spring.CommentPreviewProvider
import ge.wanderer.service.spring.command.CommandProvider
import ge.wanderer.service.spring.test_support.*
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import java.lang.IllegalStateException
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class PinServiceImplTest {

    private val pin1 = createPin(1, WARNING ,jambura(), now(), LatLng(10f, 10f), "123", "Danger here")
    private val pin2 = createPin(2, TIP , jangula(), now(), LatLng(10f, 10f), "123", "Danger here")
    private val pin3 = createPin(3, DANGER , patata(), now(), LatLng(10f, 10f), "123", "Danger here")
    private val pin4 = createPin(4, RESTING_PLACE ,jambura(), now(), LatLng(10f, 10f), "1234", "Danger here")
    private val pin5 = createPin(5, MISC_FACT , vipiSoxumski(), now(), LatLng(10f, 10f), "12345", "Danger here")

    private val pinRepository = mockedPinRepository(listOf(
        pin1, pin2, pin3, pin4, pin5
    ))
    private val reportingConfiguration = mockk<ReportingConfiguration>()
    private val userService = mockedUserService()
    private val commentRepository = mockk<CommentRepository>()

    private val service = PinServiceImpl(pinRepository, userService, CommentPreviewProvider(3), CommandProvider(), reportingConfiguration, commentRepository)

    @Test
    fun correctlyCreatesAPin() {

        every { pinRepository.persist(any()) } answers { arg(0) }

        val content = RouteElementContent("Dangerous place", "Be careful here", null)
        val createDate = dateTime("2021-03-26T10:21:11")
        val request = CreatePinRequest(createDate, 5, WARNING, content, LatLng(20f, 10f), "12345")

        val response = service.createPin(request)

        assertTrue(response.isSuccessful)
        assertEquals("Pin Created. New model persisted successfully", response.message)

        val newPin = response.data!!
        assertEquals(kalduna(), newPin.creator)
        assertEquals(createDate, newPin.createdAt)
        assertEquals(createDate, newPin.updatedAt)
        assertTrue(newPin.isActive)
        assertTrue(newPin.isRelevant)
        assertFalse(newPin.isRemoved)
        assertEquals(0, newPin.rating.totalRating)
        assertEquals(0, newPin.commentsNumber)
        assertTrue(newPin.commentsPreview.isEmpty())
        assertEquals("12345", newPin.routeCode)
        assertEquals(content.title, newPin.content.title)
        assertEquals(content.text, newPin.content.text)
        assertNull(newPin.content.attachedFile)
        assertEquals(WARNING, newPin.type)

        verify(exactly = 1) { userService.findUserById(5) }
        verify(exactly = 1) { pinRepository.persist(any()) }
    }

    @Test
    fun failsIfSomethingGoesWrongOnCommandExecution() {

        every { pinRepository.persist(any()) } throws IllegalStateException("Could not persist")
        val content = RouteElementContent("Dangerous place", "Be careful here", null)
        val createDate = dateTime("2021-03-26T10:21:11")

        val request = CreatePinRequest(createDate, 5, WARNING, content, LatLng(20f, 10f), "12345")
        val response = service.createPin(request)

        assertFalse(response.isSuccessful)
        assertEquals("Could not persist", response.message)
    }
}