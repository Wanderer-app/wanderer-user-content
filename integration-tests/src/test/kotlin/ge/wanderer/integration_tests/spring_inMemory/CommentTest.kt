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

        commentService.addComment(AddCommentRequest(commentId, "85fa0681-b7bd-4ee3-b5b5-eb2672181ae2", "atrakeb", now()))
        commentService.addComment(AddCommentRequest(commentId, "04e51444-85af-4d92-b89a-c8f761b7f3ea", "nomeri dawere", now()))
        commentService.addComment(AddCommentRequest(commentId, "b41c2dd8-db85-4d96-a1f4-92f90851f7f2", ":DDD", now()))
        commentService.addComment(AddCommentRequest(commentId, "755520ef-f06a-49e2-af7e-a0f4c19b1aba", "visa bijo", now()))
        commentService.addComment(AddCommentRequest(commentId, "5673a717-9083-4150-8b7e-c3fb25675e3a", "aeeeee", now()))

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

        commentService.giveUpVote(OperateOnContentRequest(commentId, "db9ac04e-b985-4d84-8bd4-9ce26d1f4fae", now()))
        var comment = commentService.findById(commentId, DEFAULT_LOGGED_IN_USER_ID).data!!
        assertEquals(1, comment.rating)
        assertNull(comment.userVoteDirection)

        commentService.removeVote(OperateOnContentRequest(commentId, "db9ac04e-b985-4d84-8bd4-9ce26d1f4fae", now()))
        comment = commentService.findById(commentId, DEFAULT_LOGGED_IN_USER_ID).data!!
        assertEquals(0, comment.rating)

        commentService.giveDownVote(OperateOnContentRequest(commentId, "755520ef-f06a-49e2-af7e-a0f4c19b1aba", now()))
        comment = commentService.findById(commentId, DEFAULT_LOGGED_IN_USER_ID).data!!
        assertEquals(-1, comment.rating)

        val response = commentService.giveUpVote(OperateOnContentRequest(commentId, comment.author.id, now()))
        assertFalse(response.isSuccessful)
        assertEquals("Cant vote for your own content!", response.message)
    }

    @Test
    fun canBeRemovedOrActivated() {
        commentService.remove(OperateOnContentRequest(1, "5760b116-6aab-4f04-b8be-650e27a85d09", now()))
        assertTrue(commentService.findById(1, DEFAULT_LOGGED_IN_USER_ID).data!!.isRemoved)

        commentService.activate(OperateOnContentRequest(1, "5760b116-6aab-4f04-b8be-650e27a85d09", dateTime("2021-04-04T12:00:00")))
        assertTrue(commentService.findById(1, DEFAULT_LOGGED_IN_USER_ID).data!!.isActive)
        assertEquals(dateTime("2021-04-04T12:00:00"), commentService.findById(1, DEFAULT_LOGGED_IN_USER_ID).data!!.updatedAt)

        val errorResponse = commentService.remove(OperateOnContentRequest(1, "db9ac04e-b985-4d84-8bd4-9ce26d1f4fae", now()))
        assertFalse(errorResponse.isSuccessful)
        assertEquals("You dont have rights to remove this content", errorResponse.message)
    }

    @Test
    fun canBeReported() {
        commentService.report(ReportContentRequest(1, "85fa0681-b7bd-4ee3-b5b5-eb2672181ae2", now(), ReportReason.OFFENSIVE_CONTENT))
        commentService.report(ReportContentRequest(1, "04e51444-85af-4d92-b89a-c8f761b7f3ea", now(), ReportReason.OFFENSIVE_CONTENT))
        assertTrue(commentService.findById(1, DEFAULT_LOGGED_IN_USER_ID).data!!.isActive)

        commentService.report(ReportContentRequest(1, "b41c2dd8-db85-4d96-a1f4-92f90851f7f2", now(), ReportReason.OFFENSIVE_CONTENT))
        assertTrue(commentService.findById(1, DEFAULT_LOGGED_IN_USER_ID).data!!.isRemoved)
    }

}