package ge.wanderer.integration_tests.spring_inMemory

import ge.wanderer.common.constants.TRANSIENT_ID
import ge.wanderer.common.dateTime
import ge.wanderer.common.enums.ReportReason
import ge.wanderer.common.now
import ge.wanderer.integration_tests.DEFAULT_LISTING_PARAMS
import ge.wanderer.integration_tests.DEFAULT_LOGGED_IN_USER_ID
import ge.wanderer.integration_tests.SpringServiceWithInMemoryPersistenceApp
import ge.wanderer.service.protocol.interfaces.CommentService
import ge.wanderer.service.protocol.request.AddCommentRequest
import ge.wanderer.service.protocol.request.ListCommentsRequest
import ge.wanderer.service.protocol.request.OperateOnContentRequest
import ge.wanderer.service.protocol.request.ReportContentRequest
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestPropertySource
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

@SpringBootTest(classes = [SpringServiceWithInMemoryPersistenceApp::class])
@TestPropertySource("classpath:service-spring-test.properties")
class CommentTest(
    @Autowired private val commentService: CommentService

) {

    @Test
    fun findsCommentById() {
        val response = commentService.findById(1, DEFAULT_LOGGED_IN_USER_ID)
        assertTrue(response.isSuccessful)
        assertEquals("Successfully retrieved comment", response.message)
        assertEquals(1, response.data!!.id)
    }

    @Test
    fun canBeRepliedTo() {
        val commentId = 1L

        commentService.addComment(AddCommentRequest(commentId, 2, "atrakeb", now()))
        commentService.addComment(AddCommentRequest(commentId, 3, "nomeri dawere", now()))
        commentService.addComment(AddCommentRequest(commentId, 4, ":DDD", now()))
        commentService.addComment(AddCommentRequest(commentId, 5, "visa bijo", now()))
        commentService.addComment(AddCommentRequest(commentId, 6, "aeeeee", now()))

        val commentsReplies = commentService.listComments(ListCommentsRequest(1, DEFAULT_LOGGED_IN_USER_ID, DEFAULT_LISTING_PARAMS))
        assertEquals(5, commentsReplies.resultSize)

        val comment = commentService.findById(commentId, DEFAULT_LOGGED_IN_USER_ID).data!!
        assertEquals(5, comment.responseNumber)
        assertEquals(3, comment.responsesPreview.size)
        assertTrue(comment.responsesPreview.none{ it.id == TRANSIENT_ID })
    }

    @Test
    fun canBeRated() {
        val commentId = 1L

        commentService.giveUpVote(OperateOnContentRequest(commentId, 10, now()))
        var comment = commentService.findById(commentId, DEFAULT_LOGGED_IN_USER_ID).data!!
        assertEquals(1, comment.rating)
        assertNull(comment.userVoteDirection)

        commentService.removeVote(OperateOnContentRequest(commentId, 10, now()))
        comment = commentService.findById(commentId, DEFAULT_LOGGED_IN_USER_ID).data!!
        assertEquals(0, comment.rating)

        commentService.giveDownVote(OperateOnContentRequest(commentId, 5, now()))
        comment = commentService.findById(commentId, DEFAULT_LOGGED_IN_USER_ID).data!!
        assertEquals(-1, comment.rating)

        val response = commentService.giveUpVote(OperateOnContentRequest(commentId, comment.author.id, now()))
        assertFalse(response.isSuccessful)
        assertEquals("Cant vote for your own content!", response.message)
    }

    @Test
    fun canBeRemovedOrActivated() {
        commentService.remove(OperateOnContentRequest(1, 1, now()))
        assertTrue(commentService.findById(1, DEFAULT_LOGGED_IN_USER_ID).data!!.isRemoved)

        commentService.activate(OperateOnContentRequest(1, 1, dateTime("2021-04-04T12:00:00")))
        assertTrue(commentService.findById(1, DEFAULT_LOGGED_IN_USER_ID).data!!.isActive)
        assertEquals(dateTime("2021-04-04T12:00:00"), commentService.findById(1, DEFAULT_LOGGED_IN_USER_ID).data!!.updatedAt)

        val errorResponse = commentService.remove(OperateOnContentRequest(1, 10, now()))
        assertFalse(errorResponse.isSuccessful)
        assertEquals("You dont have rights to remove this content", errorResponse.message)
    }

    @Test
    fun canBeReported() {
        commentService.report(ReportContentRequest(1, 2, now(), ReportReason.OFFENSIVE_CONTENT))
        commentService.report(ReportContentRequest(1, 3, now(), ReportReason.OFFENSIVE_CONTENT))
        assertTrue(commentService.findById(1, DEFAULT_LOGGED_IN_USER_ID).data!!.isActive)

        commentService.report(ReportContentRequest(1, 4, now(), ReportReason.OFFENSIVE_CONTENT))
        assertTrue(commentService.findById(1, DEFAULT_LOGGED_IN_USER_ID).data!!.isRemoved)
    }

}