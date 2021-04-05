package ge.wanderer.service.spring.impl

import ge.wanderer.common.dateTime
import ge.wanderer.common.enums.ReportReason
import ge.wanderer.common.now
import ge.wanderer.core.model.report.Report
import ge.wanderer.persistence.listing.ListingParams
import ge.wanderer.persistence.repository.CommentRepository
import ge.wanderer.persistence.repository.PostRepository
import ge.wanderer.service.protocol.request.*
import ge.wanderer.service.spring.command.CommandProvider
import ge.wanderer.service.spring.test_support.*
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import kotlin.test.*

class PostServiceImplTest {

    private val userService = mockedUserService()
    private val postRepository = mockk<PostRepository>()
    private val commentRepository = mockk<CommentRepository>()
    private val service = PostServiceImpl(userService, testCommentPreviewProvider(), CommandProvider(), postRepository, commentRepository, mockedReportingConfiguration())

    @Test
    fun correctlyCreatesPost() {
        every { postRepository.persist(any()) } answers { arg(0) }

        val request = CreatePostRequest(now(), 1, "123", "Some text", listOf(mockk(), mockk()))
        val response = service.createPost(request)

        assertTrue(response.isSuccessful)
        assertEquals("Post created!. New model persisted successfully", response.message)

        val data = response.data!!
        assertEquals("Some text", data.content)
        assertEquals("123", data.routeCode)
        assertEquals(jambura().id, data.creator.id)
        assertEquals(2, data.attachedFiles.size)
        assertTrue(data.isActive)
        assertFalse(data.isRemoved)

        verify(exactly = 1) { userService.findUserById(1) }
        verify(exactly = 1) { postRepository.persist(any()) }
    }

    @Test
    fun correctlyUpdatesPost() {
        every { postRepository.findById(1) } returns createNewPostWithoutFiles(1, jambura(), "Some text", now())
        val request = UpdatePostRequest(1, "Updated text", listOf(mockk()), 1)
        val response = service.updatePost(request)

        assertTrue(response.isSuccessful)
        assertEquals("POST updated", response.message)

        val data = response.data!!
        assertEquals("Updated text", data.content)
        assertEquals(jambura().id, data.creator.id)
        assertEquals(1, data.attachedFiles.size)
        assertTrue(data.isActive)
        assertFalse(data.isRemoved)
    }

    @Test
    fun findById() {
        every { postRepository.findById(1) } returns createNewPostWithoutFiles(1, jambura(), "Some text", now())

        val response = service.findById(1)
        assertTrue(response.isSuccessful)
        assertEquals("Post fetched", response.message)
        assertNotNull(response.data)
        assertEquals(1, response.data!!.id)
    }

    @Test
    fun correctlyActivatesPost() {
        val post = createNewPostWithoutFiles(1, jambura(), "Some text", now())
        post.remove(now(), jambura())
        every { postRepository.findById(1) } returns post

        val request = OperateOnContentRequest(1, 1, dateTime("2021-03-31T12:00:00"))
        val response = service.activate(request)

        assertTrue(response.isSuccessful)
        assertEquals("POST activated successfully!", response.message)

        val data = response.data!!
        assertTrue(data.isActive)
        assertFalse(data.isRemoved)
        assertEquals(dateTime("2021-03-31T12:00:00"), data.updatedAt)
    }

    @Test
    fun correctlyRemovesPost() {
        val post = createNewPostWithoutFiles(1, jambura(), "Some text", now())
        every { postRepository.findById(1) } returns post

        val request = OperateOnContentRequest(1, 1, dateTime("2021-03-31T12:00:00"))
        val response = service.remove(request)

        assertTrue(response.isSuccessful)
        assertEquals("POST removed successfully!", response.message)

        val data = response.data!!
        assertFalse(data.isActive)
        assertTrue(data.isRemoved)
        assertEquals(dateTime("2021-03-31T12:00:00"), data.updatedAt)
    }

    @Test
    fun correctlyRatesPost() {
        val post = createNewPostWithoutFiles(1, jambura(), "Some text", now())
        every { postRepository.findById(1) } returns post

        var request = OperateOnContentRequest(1, 2, now())
        var response = service.giveUpVote(request)
        assertTrue(response.isSuccessful)
        assertEquals(1, response.data!!.totalRating)

        request = OperateOnContentRequest(1, 2, now())
        response = service.giveDownVote(request)
        assertTrue(response.isSuccessful)
        assertEquals(-1, response.data!!.totalRating)

        request = OperateOnContentRequest(1, 2, now())
        response = service.removeVote(request)
        assertTrue(response.isSuccessful)
        assertEquals(0, response.data!!.totalRating)
    }

    @Test
    fun correctlyAddsCommentToPost() {
        every { postRepository.findById(1) } returns createNewPostWithoutFiles(1, jambura(), "Some text", now())

        val request = AddCommentRequest(1, 2, "aaaa", now())
        val response = service.addComment(request)

        assertTrue(response.isSuccessful)
        assertEquals("Comment added", response.message)

        val data = response.data!!
        assertEquals("aaaa", data.text)
        assertEquals(patata().id, data.author.id)
    }

    @Test
    fun correctlyListsPostComments() {
        val post = createNewPostWithoutFiles(1, jambura(), "Some text", now())
        every { postRepository.findById(1) } returns post
        every { commentRepository.listActiveFor(post, any()) } returns listOf(
            createNewComment(1, now(), "some text", patata()),
            createNewComment(2, now(), "some text", jambura()),
            createNewComment(3, now(), "some text", jangula()),
            createNewComment(4, now(), "some text", vipiSoxumski())
        )

        val response = service.listComments(1, ListingParams(5, 1, null, listOf()))

        assertTrue(response.isSuccessful)
        assertEquals("Comments Retrieved!", response.message)
        assertEquals(4, response.resultSize)
        assertEquals(4, response.data.size)
    }

    @Test
    fun correctlyReportsPost() {
        val post = createNewPostWithoutFiles(1, jambura(), "Some text", now())
        every { postRepository.findById(1) } returns post

        val request = ReportContentRequest(1, 2, now(), ReportReason.INAPPROPRIATE_CONTENT)
        val response = service.report(request)

        assertTrue(response.isSuccessful)
        assertEquals("Content Reported!", response.message)
        assertNull(response.data)
        assertEquals(1, post.reports().size)
    }

    @Test
    fun correctlyListsPostReports() {
        val post = createNewPostWithoutFiles(1, jambura(), "Some text", now())
        post.report(Report(1, jangula(), now(), ReportReason.INAPPROPRIATE_CONTENT))
        post.report(Report(2, patata(), now(), ReportReason.INAPPROPRIATE_CONTENT))

        every { postRepository.findById(1) } returns post
        val response = service.listReportsForContent(1)

        assertTrue(response.isSuccessful)
        assertEquals("Reports Retrieved!", response.message)
        assertEquals(2, response.resultSize)
        assertEquals(2, response.data.size)
    }

}