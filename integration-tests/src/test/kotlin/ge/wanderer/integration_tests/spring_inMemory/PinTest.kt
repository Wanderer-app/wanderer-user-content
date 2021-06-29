package ge.wanderer.integration_tests.spring_inMemory

import ge.wanderer.common.constants.TRANSIENT_ID
import ge.wanderer.common.dateTime
import ge.wanderer.common.enums.PinType
import ge.wanderer.common.enums.ReportReason
import ge.wanderer.common.map.LatLng
import ge.wanderer.common.now
import ge.wanderer.common.enums.VoteType
import ge.wanderer.common.enums.FileType
import ge.wanderer.integration_tests.DEFAULT_LISTING_PARAMS
import ge.wanderer.integration_tests.DEFAULT_LOGGED_IN_USER_ID
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
        var response = pinService.findById(1, DEFAULT_LOGGED_IN_USER_ID)
        assertTrue(response.isSuccessful)
        assertEquals(1, response.data!!.id)
        assertEquals("Nika",  response.data!!.creator.firstName)
        assertEquals("Jamburia",  response.data!!.creator.lastName)


        response = pinService.findById(10000, DEFAULT_LOGGED_IN_USER_ID)
        assertFalse(response.isSuccessful)
        assertEquals("Not found", response.message)
    }

    @Test
    fun canBeListed() {
        val response = pinService.list(DEFAULT_LISTING_PARAMS, DEFAULT_LOGGED_IN_USER_ID)
        assertTrue(response.isSuccessful)
        assertEquals("Pins Fetched!", response.message)
        assertEquals(5, response.resultSize)
        assertEquals(5, response.data.size)
    }

    @Test
    fun canBeListedForRoute() {
        val response = pinService.listForRoute("TB201301", DEFAULT_LISTING_PARAMS)
        assertTrue(response.isSuccessful)
        assertEquals("Pins Fetched!", response.message)
        assertTrue(response.data.isNotEmpty())
        assertTrue(response.data.all { it.routeCode == "TB201301" })
    }

    @Test
    fun canChangeStatus() {
        val pinId = 1L

        var response = pinService.remove(OperateOnContentRequest(pinId, "5760b116-6aab-4f04-b8be-650e27a85d09", now()))
        assertTrue(response.isSuccessful)
        var pin = pinService.findById(pinId, DEFAULT_LOGGED_IN_USER_ID)
        assertFalse(pin.data!!.isActive)
        assertTrue(pin.data!!.isRemoved)

        response = pinService.activate(OperateOnContentRequest(pinId, "5760b116-6aab-4f04-b8be-650e27a85d09", dateTime("2021-04-03T12:00:00")))
        assertTrue(response.isSuccessful)
        pin = pinService.findById(pinId, DEFAULT_LOGGED_IN_USER_ID)
        assertTrue(pin.data!!.isActive)
        assertFalse(pin.data!!.isRemoved)
        assertEquals(dateTime("2021-04-03T12:00:00"), pin.data!!.updatedAt)

        response = pinService.remove(OperateOnContentRequest(pinId, "755520ef-f06a-49e2-af7e-a0f4c19b1aba", dateTime("2021-04-03T12:00:00")))
        assertFalse(response.isSuccessful)
        assertEquals("You dont have rights to remove this content", response.message)

    }

    @Test
    fun canBeReportedAndThenMarkedAsIrrelevant() {
        val pinId = 1L

        var pin = pinService.reportIrrelevant(OperateOnContentRequest(pinId, "85fa0681-b7bd-4ee3-b5b5-eb2672181ae2", now())).data!!
        assertTrue(pin.isRelevant)
        assertTrue(pin.isActive)

        pin = pinService.reportIrrelevant(OperateOnContentRequest(pinId, "04e51444-85af-4d92-b89a-c8f761b7f3ea", now())).data!!
        assertTrue(pin.isRelevant)
        assertTrue(pin.isActive)

        pin = pinService.reportIrrelevant(OperateOnContentRequest(pinId, "b41c2dd8-db85-4d96-a1f4-92f90851f7f2", now())).data!!
        assertFalse(pin.isRelevant)
        assertFalse(pin.isActive)

        assertFalse(pinService.findById(pinId, DEFAULT_LOGGED_IN_USER_ID).data!!.isRelevant)
    }

    @Test
    fun canBeUpdated() {
        val response = pinService.updatePin(UpdatePinRequest(1, "New title", "New text", FileData("5760b116-6aab-4f04-b8be-650e27a85d09", FileType.IMAGE), "5760b116-6aab-4f04-b8be-650e27a85d09"))
        assertTrue(response.isSuccessful)

        val pinData = pinService.findById(1, DEFAULT_LOGGED_IN_USER_ID).data!!

        assertEquals("New title", pinData.title)
        assertEquals("New text", pinData.text)
        assertNotNull(pinData.attachedFile)
    }

    @Test
    fun canBeCreated() {
        val pinsNumberBefore = pinService.list(DEFAULT_LISTING_PARAMS, DEFAULT_LOGGED_IN_USER_ID).resultSize
        val request = CreatePinRequest(now(), "90d36b5f-e060-4f67-a4a2-c6d06ee76b04", PinType.TIP, "aq plania datesili", "xis ukanaa da morwyet xolme", FileData("5760b116-6aab-4f04-b8be-650e27a85d09", FileType.IMAGE), LatLng(10f, 10f), "1488")

        val response = pinService.createPin(request)
        assertTrue(response.isSuccessful)
        assertEquals("aq plania datesili", response.data!!.title)
        assertNotEquals(TRANSIENT_ID, response.data!!.id)
        assertEquals("xis ukanaa da morwyet xolme", response.data!!.text)
        assertEquals(LatLng(10f, 10f), response.data!!.location)
        assertEquals("1488", response.data!!.routeCode)

        assertEquals(pinsNumberBefore + 1, pinService.list(DEFAULT_LISTING_PARAMS, DEFAULT_LOGGED_IN_USER_ID).resultSize)
    }

    @Test
    fun canBeRated() {
        var pin = pinService.findById(1, DEFAULT_LOGGED_IN_USER_ID).data!!
        assertEquals(0, pin.rating.totalRating)
        assertNull(pin.userVoteDirection)

        var response = pinService.giveUpVote(OperateOnContentRequest(1, "85fa0681-b7bd-4ee3-b5b5-eb2672181ae2", now()))
        assertEquals(10, response.data!!.totalRating)

        response = pinService.giveDownVote(OperateOnContentRequest(1, "04e51444-85af-4d92-b89a-c8f761b7f3ea", now()))
        assertEquals(5, response.data!!.totalRating)

        response = pinService.giveUpVote(OperateOnContentRequest(1, "5760b116-6aab-4f04-b8be-650e27a85d09", now()))
        assertFalse(response.isSuccessful)
        assertEquals("Cant vote for your own content!", response.message)

        pin = pinService.findById(1, "85fa0681-b7bd-4ee3-b5b5-eb2672181ae2").data!!
        assertEquals(5, pin.rating.totalRating)
        assertEquals(VoteType.UP, pin.userVoteDirection)
    }

    @Test
    fun canBeCommented() {
        val commentsBefore = pinService.findById(1, DEFAULT_LOGGED_IN_USER_ID).data!!.commentsPreview.size
        pinService.addComment(AddCommentRequest(1, "85fa0681-b7bd-4ee3-b5b5-eb2672181ae2", "maladec sheen", now()))
        pinService.addComment(AddCommentRequest(1, "5673a717-9083-4150-8b7e-c3fb25675e3a", "madloba", now()))

        val pin = pinService.findById(1, DEFAULT_LOGGED_IN_USER_ID).data!!
        assertEquals(commentsBefore + 2, pin.commentsPreview.size)
        assertTrue(pin.commentsPreview.none { it.id == TRANSIENT_ID })
    }

    @Test
    fun canBeReportedAndThenRemoved() {
        pinService.report(ReportContentRequest(2, "85fa0681-b7bd-4ee3-b5b5-eb2672181ae2", now(), ReportReason.INAPPROPRIATE_CONTENT))
        pinService.report(ReportContentRequest(2, "04e51444-85af-4d92-b89a-c8f761b7f3ea", now(), ReportReason.INAPPROPRIATE_CONTENT))

        assertTrue(pinService.findById(2, DEFAULT_LOGGED_IN_USER_ID).data!!.isActive)

        val response = pinService.report(ReportContentRequest(2, "04e51444-85af-4d92-b89a-c8f761b7f3ea", now(), ReportReason.INAPPROPRIATE_CONTENT))
        assertFalse(response.isSuccessful)
        assertEquals("You already reported this content", response.message)

        pinService.report(ReportContentRequest(2, "755520ef-f06a-49e2-af7e-a0f4c19b1aba", now(), ReportReason.OFFENSIVE_CONTENT))
        val pinData = pinService.findById(2, DEFAULT_LOGGED_IN_USER_ID).data!!

        assertFalse(pinData.isActive)
        assertTrue(pinData.isRemoved)
    }

}