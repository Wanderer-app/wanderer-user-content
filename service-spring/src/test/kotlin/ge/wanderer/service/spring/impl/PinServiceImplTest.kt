package ge.wanderer.service.spring.impl

import ge.wanderer.common.dateTime
import ge.wanderer.common.listing.ListingParams
import ge.wanderer.common.map.LatLng
import ge.wanderer.common.now
import ge.wanderer.core.configuration.ReportingConfiguration
import ge.wanderer.core.data.file.AttachedFile
import ge.wanderer.core.model.map.MarkerType.*
import ge.wanderer.core.model.map.RouteElementContent
import ge.wanderer.core.model.report.Report
import ge.wanderer.core.model.report.ReportReason
import ge.wanderer.core.repository.CommentRepository
import ge.wanderer.service.protocol.request.*
import ge.wanderer.service.spring.command.CommandProvider
import ge.wanderer.service.spring.test_support.*
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
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

    private val service = PinServiceImpl(pinRepository, userService, testCommentPreviewProvider(), CommandProvider(), reportingConfiguration, commentRepository)

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

    @Test
    fun correctlyListsPinsByRoute() {
        every { pinRepository.listForRoute("123", any()) } returns listOf(pin1, pin2, pin3)

        val listingParams = ListingParams(5, 1, null, listOf())
        val response = service.listForRoute("123", listingParams)

        assertTrue(response.isSuccessful)
        assertEquals("Pins Fetched!", response.message)
        assertEquals(3, response.resultSize)
        assertEquals(3, response.data.size)
        assertTrue(response.data.all { it.routeCode == "123" })
        verify(exactly = 1) { pinRepository.listForRoute("123", listingParams) }
    }

    @Test
    fun correctlyListsPins() {
        val listingParams = ListingParams(5, 1, null, listOf())
        val response = service.list(listingParams)

        assertTrue(response.isSuccessful)
        assertEquals("Pins Fetched!", response.message)
        assertEquals(5, response.resultSize)
        assertEquals(5, response.data.size)
        verify(exactly = 1) { pinRepository.list(listingParams) }
    }

    @Test
    fun correctlyReportsPinAsIrrelevant() {
        every { reportingConfiguration.shouldBeMarkedIrrelevant(pin5) } returns false

        val request = OperateOnContentRequest(5, 1, dateTime("2021-03-27T17:00:00"))
        val response = service.reportIrrelevant(request)

        assertTrue(response.isSuccessful)
        assertEquals("Content Reported!", response.message)
        assertEquals(1, pin5.reports().size)
        assertEquals(ReportReason.IRRELEVANT, pin5.reports().first().reason)
        verify(exactly = 1) { reportingConfiguration.shouldBeMarkedIrrelevant(pin5) }
    }

    @Test
    fun correctlyReportsAndMarksPinAsIrrelevant() {
        every { reportingConfiguration.shouldBeMarkedIrrelevant(pin5) } returns true

        val request = OperateOnContentRequest(5, 1, dateTime("2021-03-27T17:00:00"))
        val response = service.reportIrrelevant(request)

        assertTrue(response.isSuccessful)
        assertEquals("Content Reported!", response.message)
        assertFalse(response.data!!.isActive)
        assertFalse(response.data!!.isRelevant)

        assertEquals(1, pin5.reports().size)
        assertEquals(ReportReason.IRRELEVANT, pin5.reports().first().reason)
        assertEquals(dateTime("2021-03-27T17:00:00"), pin5.statusUpdatedAt())
        assertFalse(pin5.isRelevant())
        assertFalse(pin5.isActive())

        verify(exactly = 1) { reportingConfiguration.shouldBeMarkedIrrelevant(pin5) }
        verify(exactly = 1) { userService.notifyContentStatusChange(pin5) }
    }

    @Test
    fun correctlyUpdatesPin() {
        val attachedFile = mockk<AttachedFile>()
        val newContent = RouteElementContent("aaaa", "aaaa", attachedFile)
        val request = UpdatePinRequest(5, newContent, 4)

        val response = service.updatePin(request)
        assertTrue(response.isSuccessful)
        assertEquals("Pin updated", response.message)

        val updatedPin = response.data!!
        assertEquals(5, updatedPin.id)
        assertEquals(attachedFile, updatedPin.content.attachedFile)
        assertEquals("aaaa", updatedPin.content.text)
        assertEquals("aaaa", updatedPin.content.title)
    }

    @Test
    fun findsById() {
        val response = service.findById(1)
        assertTrue(response.isSuccessful)
        assertEquals("Pin fetched!", response.message)
        assertEquals(1, response.data!!.id)
    }

    @Test
    fun correctlyActivatesPin() {
        pin1.remove(now(), jambura())
        val request = OperateOnContentRequest(1, 1, dateTime("2021-03-27T12:00:00"))
        val response = service.activate(request)

        assertTrue(response.isSuccessful)
        assertEquals("PIN activated successfully!", response.message)
        assertEquals(1, response.data!!.id)
        assertTrue(response.data!!.isActive)
        assertEquals(dateTime("2021-03-27T12:00:00"), response.data!!.updatedAt)
    }

    @Test
    fun correctlyRemovesPin() {
        val request = OperateOnContentRequest(5, 2, dateTime("2021-03-27T12:00:00"))
        val response = service.remove(request)

        assertTrue(response.isSuccessful)
        assertEquals("PIN removed successfully!", response.message)
        assertEquals(5, response.data!!.id)
        assertTrue(response.data!!.isRemoved)
        assertEquals(dateTime("2021-03-27T12:00:00"), response.data!!.updatedAt)

        verify(exactly = 1) { userService.notifyContentStatusChange(pin5) }
    }

    @Test
    fun correctlyRatesPin() {
        var request = OperateOnContentRequest(1, 2, now())
        var response = service.giveUpVote(request)
        assertTrue(response.isSuccessful)
        assertEquals(5, response.data!!.totalRating)
        assertEquals(5, pin1.rating())

        request = OperateOnContentRequest(1, 4, now())
        response = service.giveUpVote(request)
        assertTrue(response.isSuccessful)
        assertEquals(6, response.data!!.totalRating)
        assertEquals(6, pin1.rating())

        request = OperateOnContentRequest(1, 2, now())
        response = service.giveDownVote(request)
        assertTrue(response.isSuccessful)
        assertEquals(-4, response.data!!.totalRating)
        assertEquals(-4, pin1.rating())

        request = OperateOnContentRequest(1, 2, now())
        response = service.removeVote(request)
        assertTrue(response.isSuccessful)
        assertEquals(1, response.data!!.totalRating)
        assertEquals(1, pin1.rating())

        request = OperateOnContentRequest(1, 1, now())
        response = service.giveUpVote(request)
        assertFalse(response.isSuccessful)
        assertEquals("Cant vote for your own content!", response.message)
    }

    @Test
    fun correctlyAddsCommentsToPin() {
        val request = AddCommentRequest(1, 2, "maladeeec", now())
        val response = service.addComment(request)

        assertTrue(response.isSuccessful)
        assertEquals("Comment added", response.message)
        assertEquals("maladeeec", response.data!!.text)
        assertEquals(patata(), response.data!!.author)
        verify(exactly = 1) { userService.notifyContentWasCommented(pin1, any()) }
    }


    @Test
    fun correctlyListsPinComments() {
        pin2.addComment(createNewComment(1, now(), "alioo", jambura()))
        pin2.addComment(createNewComment(2, now(), "qaia", jangula()))
        every { commentRepository.listActiveFor(pin2, any()) } returns pin2.comments()

        val listingParams = ListingParams(4, 1, null, listOf())
        val response = service.listComments(2, listingParams)

        assertTrue(response.isSuccessful)
        assertEquals("Comments fetched!", response.message)
        assertEquals(1, response.batchNumber)
        assertEquals(2, response.resultSize)

        assertEquals(2, response.data.size)
        assertEquals("alioo", response.data[0].text)
        assertEquals("qaia", response.data[1].text)
    }

    @Test
    fun correctlyReportsAPin() {
        every { reportingConfiguration.shouldBeRemoved(any()) } returns false
        every { reportingConfiguration.shouldNotifyAdministration(any()) } returns true

        val request = ReportContentRequest(1, 5, now(), ReportReason.INAPPROPRIATE_CONTENT)
        val response = service.report(request)

        assertTrue(response.isSuccessful)
        assertEquals("Content Reported!", response.message)
        assertEquals(1, pin1.reports().size)
        assertEquals(ReportReason.INAPPROPRIATE_CONTENT, pin1.reports().first().reason)
        assertEquals(kalduna(), pin1.reports().first().creator)
        verify(exactly = 1) { userService.notifyAdministrationAboutReport(pin1) }
    }

    @Test
    fun throwsErrorWhenIncorrectlyReportingIrrelevantPin() {
        every { reportingConfiguration.shouldBeRemoved(any()) } returns false
        every { reportingConfiguration.shouldNotifyAdministration(any()) } returns true

        val request = ReportContentRequest(1, 5, now(), ReportReason.IRRELEVANT)

        val exception = assertThrows<IllegalStateException> { service.report(request) }
        assertEquals("Use reportIrrelevant() method for reporting a pin as irrelevant", exception.message!!)
    }

    @Test
    fun correctlyListsReports() {
        pin1.report(Report(1, jangula(), now(), ReportReason.OFFENSIVE_CONTENT))
        pin1.report(Report(2, patata(), now(), ReportReason.OFFENSIVE_CONTENT))
        pin1.report(Report(3, vipiSoxumski(), now(), ReportReason.OFFENSIVE_CONTENT))

        val response = service.listReportsForContent(1)

        assertTrue(response.isSuccessful)
        assertEquals("Reports Retrieved!", response.message)
        assertEquals(3, response.resultSize)
        assertEquals(3, response.data.size)
        assertTrue(response.data.all { it.reason == ReportReason.OFFENSIVE_CONTENT })
    }
}