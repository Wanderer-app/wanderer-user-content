package ge.wanderer.integration_tests.spring_inMemory

import ge.wanderer.common.constants.TRANSIENT_ID
import ge.wanderer.common.dateTime
import ge.wanderer.common.enums.PinType
import ge.wanderer.common.enums.ReportReason
import ge.wanderer.common.map.LatLng
import ge.wanderer.common.now
import ge.wanderer.integration_tests.SpringServiceWithInMemoryPersistenceApp
import ge.wanderer.service.protocol.data.FileData
import ge.wanderer.service.protocol.interfaces.PinService
import ge.wanderer.service.protocol.request.*
import io.mockk.mockk
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestPropertySource
import kotlin.test.*

@SpringBootTest(classes = [SpringServiceWithInMemoryPersistenceApp::class])
@TestPropertySource("classpath:service-spring-test.properties")
class PinTest(
    @Autowired private val pinService: PinService
) {

    @Test
    fun canBeRetrievedById() {
        var response = pinService.findById(1)
        assertTrue(response.isSuccessful)
        assertEquals(1, response.data!!.id)
        assertEquals("Nika Jamburia",  response.data!!.creator.fullName)


        response = pinService.findById(10000)
        assertFalse(response.isSuccessful)
        assertEquals("Not found", response.message)
    }

    @Test
    fun canBeListed() {
        val response = pinService.list(DEFAULT_LISTING_PARAMS)
        assertTrue(response.isSuccessful)
        assertEquals("Pins Fetched!", response.message)
        assertEquals(5, response.resultSize)
        assertEquals(5, response.data.size)
    }

    @Test
    fun canBeListedForRoute() {
        val response = pinService.listForRoute("123", DEFAULT_LISTING_PARAMS)
        assertTrue(response.isSuccessful)
        assertEquals("Pins Fetched!", response.message)
        assertTrue(response.data.isNotEmpty())
        assertTrue(response.data.all { it.routeCode == "123" })
    }

    @Test
    fun canChangeStatus() {
        val pinId = 1L

        var response = pinService.remove(OperateOnContentRequest(pinId, 1, now()))
        assertTrue(response.isSuccessful)
        var pin = pinService.findById(pinId)
        assertFalse(pin.data!!.isActive)
        assertTrue(pin.data!!.isRemoved)

        response = pinService.activate(OperateOnContentRequest(pinId, 1, dateTime("2021-04-03T12:00:00")))
        assertTrue(response.isSuccessful)
        pin = pinService.findById(pinId)
        assertTrue(pin.data!!.isActive)
        assertFalse(pin.data!!.isRemoved)
        assertEquals(dateTime("2021-04-03T12:00:00"), pin.data!!.updatedAt)

        response = pinService.remove(OperateOnContentRequest(pinId, 5, dateTime("2021-04-03T12:00:00")))
        assertFalse(response.isSuccessful)
        assertEquals("You dont have rights to remove this content", response.message)

    }

    @Test
    fun canBeReportedAndThenMarkedAsIrrelevant() {
        val pinId = 1L

        var pin = pinService.reportIrrelevant(OperateOnContentRequest(pinId, 2, now())).data!!
        assertTrue(pin.isRelevant)
        assertTrue(pin.isActive)

        pin = pinService.reportIrrelevant(OperateOnContentRequest(pinId, 3, now())).data!!
        assertTrue(pin.isRelevant)
        assertTrue(pin.isActive)

        pin = pinService.reportIrrelevant(OperateOnContentRequest(pinId, 4, now())).data!!
        assertFalse(pin.isRelevant)
        assertFalse(pin.isActive)

        assertFalse(pinService.findById(pinId).data!!.isRelevant)
    }

    @Test
    fun canBeUpdated() {
        val response = pinService.updatePin(UpdatePinRequest(1, "New title", "New text", mockk(), 1))
        assertTrue(response.isSuccessful)

        val pinData = pinService.findById(1).data!!

        assertEquals("New title", pinData.title)
        assertEquals("New text", pinData.text)
        assertNotNull(pinData.attachedFile)
    }

    @Test
    fun canBeCreated() {
        val pinsNumberBefore = pinService.list(DEFAULT_LISTING_PARAMS).resultSize
        val request = CreatePinRequest(now(), 7, PinType.TIP, "aq plania datesili", "xis ukanaa da morwyet xolme", FileData(), LatLng(10f, 10f), "1488")

        val response = pinService.createPin(request)
        assertTrue(response.isSuccessful)
        assertEquals("aq plania datesili", response.data!!.title)
        assertNotEquals(TRANSIENT_ID, response.data!!.id)
        assertEquals("xis ukanaa da morwyet xolme", response.data!!.text)
        assertEquals(LatLng(10f, 10f), response.data!!.location)
        assertEquals("1488", response.data!!.routeCode)

        assertEquals(pinsNumberBefore + 1, pinService.list(DEFAULT_LISTING_PARAMS).resultSize)
    }

    @Test
    fun canBeRated() {
        var pin = pinService.findById(1).data!!
        assertEquals(0, pin.rating.totalRating)

        var response = pinService.giveUpVote(OperateOnContentRequest(1, 2, now()))
        assertEquals(10, response.data!!.totalRating)

        response = pinService.giveDownVote(OperateOnContentRequest(1, 3, now()))
        assertEquals(5, response.data!!.totalRating)

        response = pinService.giveUpVote(OperateOnContentRequest(1, 1, now()))
        assertFalse(response.isSuccessful)
        assertEquals("Cant vote for your own content!", response.message)

        pin = pinService.findById(1).data!!
        assertEquals(5, pin.rating.totalRating)
    }

    @Test
    fun canBeCommented() {
        val commentsBefore = pinService.findById(1).data!!.commentsPreview.size
        pinService.addComment(AddCommentRequest(1, 2, "maladec sheen", now()))
        pinService.addComment(AddCommentRequest(1, 6, "madloba", now()))

        val pin = pinService.findById(1).data!!
        assertEquals(commentsBefore + 2, pin.commentsPreview.size)
        assertTrue(pin.commentsPreview.none { it.id == TRANSIENT_ID })
    }

    @Test
    fun canBeReportedAndThenRemoved() {
        pinService.report(ReportContentRequest(2, 2, now(), ReportReason.INAPPROPRIATE_CONTENT))
        pinService.report(ReportContentRequest(2, 3, now(), ReportReason.INAPPROPRIATE_CONTENT))

        assertTrue(pinService.findById(2).data!!.isActive)

        val response = pinService.report(ReportContentRequest(2, 3, now(), ReportReason.INAPPROPRIATE_CONTENT))
        assertFalse(response.isSuccessful)
        assertEquals("You already reported this content", response.message)

        pinService.report(ReportContentRequest(2, 5, now(), ReportReason.OFFENSIVE_CONTENT))
        val pinData = pinService.findById(2).data!!

        assertFalse(pinData.isActive)
        assertTrue(pinData.isRemoved)
    }

}