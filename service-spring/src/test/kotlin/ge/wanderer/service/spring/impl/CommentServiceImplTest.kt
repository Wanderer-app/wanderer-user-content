package ge.wanderer.service.spring.impl

import ge.wanderer.common.dateTime
import ge.wanderer.common.enums.ReportReason
import ge.wanderer.common.now
import ge.wanderer.core.configuration.ReportingConfiguration
import ge.wanderer.common.listing.ListingParams
import ge.wanderer.service.protocol.request.AddCommentRequest
import ge.wanderer.service.protocol.request.OperateOnContentRequest
import ge.wanderer.service.protocol.request.ReportContentRequest
import ge.wanderer.service.protocol.request.UpdateCommentRequest
import ge.wanderer.service.spring.command.CommandProvider
import ge.wanderer.service.spring.test_support.*
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class CommentServiceImplTest {

    private val comment1 = createNewComment(1, now(), "Privet", jambura())
    private val comment2 = createNewComment(2, now(), "zx", jangula())
    private val comment3 = createNewComment(3, now(), "baro baro", patata())
    private val comment4 = createNewComment(4, now(), "sadaa chemi 10 maneti", kalduna())

    private val commentRepository = mockedCommentRepository(listOf(comment1, comment2, comment3, comment4))
    private val userService = mockedUserService()

    private val commandProvider = CommandProvider()
    private val commentPreviewProvider = testCommentPreviewProvider()
    private val reportingConfiguration = mockk<ReportingConfiguration> {
        every { shouldBeRemoved(any()) } returns false
        every { shouldNotifyAdministration(any()) } returns false
    }

    private val service = CommentServiceImpl(commentRepository, userService, commandProvider, commentPreviewProvider, reportingConfiguration)

    @Test
    fun correctlyUpdatesComments() {
        val request = UpdateCommentRequest(4, 5, "sadaa chemi 1000 maneti?")

        val response = service.updateComment(request)
        assertTrue(response.isSuccessful)
        assertEquals("Comment updated", response.message)

        val commentData = response.data!!
        assertEquals(4, commentData.id)
        assertEquals(kalduna().id, commentData.author.id)
        assertEquals("sadaa chemi 1000 maneti?", commentData.text)

        verify(exactly = 1) { commentRepository.findById(4) }
        verify(exactly = 1) { userService.findUserById(5) }
    }

    @Test
    fun correctlyHandlesErrorOnUpdate() {
        val request = UpdateCommentRequest(4, 1, "sadaa chemi 1 maneti?")

        val response = service.updateComment(request)
        assertFalse(response.isSuccessful)
        assertEquals("You can't update this comment", response.message)
        assertEquals("sadaa chemi 10 maneti", response.data!!.text)
    }

    @Test
    fun correctlyFindsById() {
        val response = service.findById(3)
        assertTrue(response.isSuccessful)
        assertEquals("Successfully retrieved comment", response.message)
        assertEquals(3, response.data!!.id)
        assertEquals(patata().id, response.data!!.author.id)
        verify(exactly = 1) { commentRepository.findById(3) }
    }

    @Test
    fun correctlyActivatesComment() {
        comment1.remove(dateTime("2021-03-29"), jambura())
        val request = OperateOnContentRequest(1, 1, dateTime("2021-03-30"))

        val result = service.activate(request)
        assertTrue(result.isSuccessful)
        assertEquals("COMMENT activated successfully!", result.message)

        val commentData = result.data!!
        assertEquals(1, commentData.id)
        assertEquals(jambura().id, commentData.author.id)
        assertTrue(commentData.isActive)
        assertEquals(dateTime("2021-03-30"), commentData.updatedAt)
    }

    @Test
    fun returnsCorrectResponseOnActivationError() {
        comment1.remove(dateTime("2021-03-29"), jambura())
        val request = OperateOnContentRequest(1, 5, dateTime("2021-03-30"))

        val result = service.activate(request)
        assertFalse(result.isSuccessful)
        assertEquals("You dont have rights to activate this content", result.message)
    }

    @Test
    fun correctlyRemovesContent() {
        val request = OperateOnContentRequest(4, 2, dateTime("2021-03-30"))

        val result = service.remove(request)

        assertTrue(result.isSuccessful)
        assertEquals("COMMENT removed successfully!", result.message)

        val commentData = result.data!!
        assertEquals(4, commentData.id)
        assertEquals(kalduna().id, commentData.author.id)
        assertFalse(commentData.isActive)
        assertTrue(commentData.isRemoved)
        assertEquals(dateTime("2021-03-30"), commentData.updatedAt)
    }

    @Test
    fun correctlyRatesContent() {
        // user(id=2) up votes comment(id=1)
        var request = OperateOnContentRequest(1, 2, now())
        var result = service.giveUpVote(request)
        assertTrue(result.isSuccessful)
        assertEquals(1, result.data!!.totalRating)

        // user(id=3) up votes comment(id=1)
        request = OperateOnContentRequest(1, 3, now())
        result = service.giveUpVote(request)
        assertTrue(result.isSuccessful)
        assertEquals(2, result.data!!.totalRating)

        // user(id=5) down votes comment(id=1)
        request = OperateOnContentRequest(1, 5, now())
        result = service.giveDownVote(request)
        assertTrue(result.isSuccessful)
        assertEquals(1, result.data!!.totalRating)

        // user(id=1) tries to up vote own comment(id=1)
        request = OperateOnContentRequest(1, 1, now())
        result = service.giveDownVote(request)
        assertFalse(result.isSuccessful)
        assertEquals("Cant vote for your own content!", result.message)
        assertEquals(1, result.data!!.totalRating)

        // user(id=3) removes his up vote
        request = OperateOnContentRequest(1, 3, now())
        result = service.removeVote(request)
        assertTrue(result.isSuccessful)
        assertEquals(0, result.data!!.totalRating)
    }

    @Test
    fun correctlyAddsRepliesToComments() {
        var request = AddCommentRequest(4, 1, "lashas mamas hqonda", now())
        var response = service.addComment(request)

        assertTrue(response.isSuccessful)
        assertEquals("Comment added", response.message)
        assertEquals("lashas mamas hqonda", response.data!!.text)

        request = AddCommentRequest(4, 5, "axla atrakeb", now())
        response = service.addComment(request)

        assertTrue(response.isSuccessful)
        assertEquals("Comment added", response.message)
        assertEquals("axla atrakeb", response.data!!.text)
    }

    @Test
    fun correctlyListsReplies() {
        comment1.addComment(createNewComment(5, now(), "alioooo", patata()))
        comment1.addComment(createNewComment(6, now(), "rogor xar lamaso", vipiSoxumski()))
        comment1.addComment(createNewComment(7, now(), "sd", jangula()))

        every { commentRepository.listActiveFor(comment1, any()) } returns comment1.comments()
        val response = service.listComments(1, ListingParams(5, 1, null, listOf()))
        assertTrue(response.isSuccessful)

        val replies = response.data
        assertEquals(3, replies.size)
    }

    @Test
    fun correctlyReportsComments() {
        val request = ReportContentRequest(4, 1, now(), ReportReason.INAPPROPRIATE_CONTENT)
        val response = service.report(request)

        assertTrue(response.isSuccessful)
        assertEquals("Content Reported!", response.message)
        assertEquals(1, comment4.reports().size)
    }

    @Test
    fun administrationCanBeNotifiedAndContentRemovedOnReport() {
        val request = ReportContentRequest(4, 1, now(), ReportReason.INAPPROPRIATE_CONTENT)
        val response = service.report(request)

        assertTrue(response.isSuccessful)
        assertEquals("Content Reported!", response.message)
        assertEquals(1, comment4.reports().size)
        verify(exactly = 1) { reportingConfiguration.shouldBeRemoved(comment4) }
        verify(exactly = 1) { reportingConfiguration.shouldNotifyAdministration(comment4) }

        val reportsResponse = service.listReportsForContent(4)
        assertTrue(reportsResponse.isSuccessful)
        assertEquals(1, reportsResponse.data.size)
        assertEquals(jambura(), reportsResponse.data.first().creator)
        assertEquals(ReportReason.INAPPROPRIATE_CONTENT, reportsResponse.data.first().reason)
    }

}