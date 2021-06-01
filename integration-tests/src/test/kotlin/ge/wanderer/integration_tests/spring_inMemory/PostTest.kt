package ge.wanderer.integration_tests.spring_inMemory

import ge.wanderer.common.constants.TRANSIENT_ID
import ge.wanderer.common.dateTime
import ge.wanderer.common.enums.ReportReason
import ge.wanderer.common.now
import ge.wanderer.common.enums.FileType
import ge.wanderer.integration_tests.DEFAULT_LISTING_PARAMS
import ge.wanderer.integration_tests.DEFAULT_LOGGED_IN_USER_ID
import ge.wanderer.integration_tests.SpringServiceWithInMemoryPersistenceApp
import ge.wanderer.service.protocol.data.FileData
import ge.wanderer.service.protocol.interfaces.PostService
import ge.wanderer.service.protocol.request.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestPropertySource
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@SpringBootTest(classes = [SpringServiceWithInMemoryPersistenceApp::class])
@TestPropertySource("classpath:service-spring-test.properties")
class PostTest(
    @Autowired private val postService: PostService
) {

    @Test
    fun postCanBeCreated() {
        val request = CreatePostRequest(now(), 1, "123", "Some teeext", listOf(FileData("1", FileType.IMAGE), FileData("2", FileType.IMAGE)))
        val response = postService.createPost(request)
        assertTrue(response.isSuccessful)
        assertEquals("Post created!. New model persisted successfully", response.message)

        val post = response.data!!
        assertEquals("Some teeext", post.content)
        assertEquals(2, post.attachedFiles.size)
        assertEquals("Some teeext", postService.findById(post.id, DEFAULT_LOGGED_IN_USER_ID).data!!.content)
    }

    @Test
    fun canBeUpdated() {
        val post = postService.createPost(
            CreatePostRequest(now(), 1, "123", "Some teeext", listOf(FileData("1", FileType.IMAGE), FileData("1", FileType.IMAGE)))
        ).data!!

        val request = UpdatePostRequest(post.id, "Updated text", listOf(), post.creator.id)
        val response = postService.updatePost(request)

        assertTrue(response.isSuccessful)
        assertEquals("POST updated", response.message)
        assertEquals("Updated text", response.data!!.content)
        assertTrue(response.data!!.attachedFiles.isEmpty())
    }

    @Test
    fun canBeRated() {
        var post = postService.findById(1, DEFAULT_LOGGED_IN_USER_ID).data!!
        assertEquals(0, post.ratingData!!.totalRating)

        var response = postService.giveUpVote(OperateOnContentRequest(post.id, 2, now()))
        assertEquals(1, response.data!!.totalRating)

        response = postService.giveDownVote(OperateOnContentRequest(post.id, 3, now()))
        assertEquals(0, response.data!!.totalRating)

        response = postService.giveUpVote(OperateOnContentRequest(post.id, post.creator.id, now()))
        assertFalse(response.isSuccessful)
        assertEquals("Cant vote for your own content!", response.message)

        response = postService.removeVote(OperateOnContentRequest(post.id, 2, now()))
        assertTrue(response.isSuccessful)

        post = postService.findById(1, DEFAULT_LOGGED_IN_USER_ID).data!!
        assertEquals(-1, post.ratingData!!.totalRating)
    }

    @Test
    fun canBeCommented() {
        val commentsBefore = postService.findById(1, DEFAULT_LOGGED_IN_USER_ID).data!!.commentsAmount
        postService.addComment(AddCommentRequest(1, 2, "maladec sheen", now()))
        postService.addComment(AddCommentRequest(1, 6, "madloba", now()))

        val comments = postService.listComments(ListCommentsRequest(1, DEFAULT_LOGGED_IN_USER_ID, DEFAULT_LISTING_PARAMS)).data
        assertEquals(commentsBefore + 2, comments.size)
        assertTrue(comments.none { it.id == TRANSIENT_ID })
    }

    @Test
    fun canBeReportedAndThenRemoved() {
        postService.report(ReportContentRequest(2, 2, now(), ReportReason.INAPPROPRIATE_CONTENT))
        postService.report(ReportContentRequest(2, 3, now(), ReportReason.INAPPROPRIATE_CONTENT))

        assertTrue(postService.findById(2, DEFAULT_LOGGED_IN_USER_ID).data!!.isActive)
        assertEquals(2, postService.listReportsForContent(2).data.size)

        val response = postService.report(ReportContentRequest(2, 3, now(), ReportReason.INAPPROPRIATE_CONTENT))
        assertFalse(response.isSuccessful)
        assertEquals("You already reported this content", response.message)

        postService.report(ReportContentRequest(2, 5, now(), ReportReason.OFFENSIVE_CONTENT))
        val post = postService.findById(2, DEFAULT_LOGGED_IN_USER_ID).data!!

        assertFalse(post.isActive)
        assertTrue(post.isRemoved)
    }

    @Test
    fun canBeRemovedAndActivated() {
        val post = postService.createPost(
            CreatePostRequest(now(), 1, "123", "Some teeext", listOf(FileData("1", FileType.IMAGE), FileData("1", FileType.IMAGE)))
        ).data!!

        val removeResponse = postService.remove(OperateOnContentRequest(post.id, post.creator.id, now()))
        assertTrue(removeResponse.isSuccessful)
        assertEquals("POST removed successfully!", removeResponse.message)
        assertTrue(removeResponse.data!!.isRemoved)

        val failResponse = postService.activate(OperateOnContentRequest(post.id, 5, now()))
        assertFalse(failResponse.isSuccessful)
        assertEquals("You dont have rights to activate this content", failResponse.message)

        val activateResponse = postService.activate(OperateOnContentRequest(post.id, post.creator.id, dateTime("2021-04-04T12:00:00")))
        assertTrue(activateResponse.isSuccessful)
        assertEquals("POST activated successfully!", activateResponse.message)
        assertEquals(dateTime("2021-04-04T12:00:00"), activateResponse.data!!.updatedAt)
        assertTrue(activateResponse.data!!.isActive)
    }
}