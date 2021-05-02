package ge.wanderer.service.spring.impl.decorator

import ge.wanderer.common.listing.ListingParams
import ge.wanderer.service.protocol.request.ListCommentsRequest
import ge.wanderer.service.protocol.response.ServiceListingResponse
import ge.wanderer.service.protocol.response.ServiceResponse
import ge.wanderer.service.spring.impl.*
import ge.wanderer.service.spring.test_support.DEFAULT_LOGGED_IN_USER_ID
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test
import kotlin.test.*

class ExceptionHandlingServiceDecoratorTest {

    @Test
    fun correctlyHandlesCommentService() {

        val decoratedService = mockk<CommentServiceImpl> {
            every { updateComment(any()) } throws IllegalStateException("Wrong input")
            every { listComments(any()) } throws IllegalStateException("Comment not found")
            every { findById(any(), any()) } throws Exception()
            every { addComment(any()) } returns ServiceResponse(true, "Operation successful", mockk())
        }
        val decorator = ExceptionHandlingCommentService(decoratedService)

        val updateResult = decorator.updateComment(mockk())
        assertFalse(updateResult.isSuccessful)
        assertEquals( "Wrong input", updateResult.message)
        assertNull(updateResult.data)

        val listingResult = decorator.listComments(ListCommentsRequest(1, 1, mockk()))
        assertFalse(listingResult.isSuccessful)
        assertEquals( "Comment not found", listingResult.message)
        assertTrue(listingResult.data.isEmpty())

        val findByIdResult = decorator.findById(1, DEFAULT_LOGGED_IN_USER_ID)
        assertFalse(findByIdResult.isSuccessful)
        assertTrue(findByIdResult.message.startsWith("Exception occurred: java.lang.Exception"))
        assertNull(findByIdResult.data)

        val successfulResult = decorator.addComment(mockk())
        assertTrue(successfulResult.isSuccessful)
        assertEquals("Operation successful", successfulResult.message)
        assertNotNull(successfulResult.data)
    }

    @Test
    fun correctlyHandlesDiscussionService() {

        val service = mockk<DiscussionServiceImpl> {
            every { getDiscussionForRoute("123", any(), any()) } returns ServiceListingResponse(true, "Discussions fetched", 1, 1, listOf(mockk()))
            every { getDiscussionForRoute("1234", any(), any()) } throws IllegalStateException("Route not found")
            every { getDiscussionForRoute("1235", any(), any()) } throws IllegalStateException()
        }
        val decorator = ExceptionHandlingDiscussionService(service)

        var result = decorator.getDiscussionForRoute("1234", DEFAULT_LOGGED_IN_USER_ID, mockk())
        assertFalse(result.isSuccessful)
        assertEquals("Route not found", result.message)
        assertTrue(result.data.isEmpty())

        val listingParams = ListingParams(1, 1, null, listOf())
        result = decorator.getDiscussionForRoute("123", 1, listingParams)
        assertTrue(result.isSuccessful)
        assertEquals("Discussions fetched", result.message)
        assertFalse(result.data.isEmpty())

        result = decorator.getDiscussionForRoute("1235", 1, mockk())
        assertFalse(result.isSuccessful)
        assertTrue(result.message.startsWith("Exception occurred: java.lang.IllegalStateException"))
        assertTrue(result.data.isEmpty())
    }

    @Test
    fun correctlyHandlesPinService() {

        val service = mockk<PinServiceImpl> {
            every { findById(1, DEFAULT_LOGGED_IN_USER_ID) } returns ServiceResponse(true, "fetched", mockk())
            every { findById(2, DEFAULT_LOGGED_IN_USER_ID) } throws IllegalStateException("Not found")
            every { listForRoute("123", any()) } returns ServiceListingResponse(true, "pins fetched",  2, 2, listOf(mockk(), mockk()))
            every { listForRoute("1234", any()) } throws IllegalStateException("Route Not found")
        }
        val decorator = ExceptionHandlingPinService(service)

        var response = decorator.findById(1, DEFAULT_LOGGED_IN_USER_ID)
        assertTrue(response.isSuccessful)
        assertEquals("fetched", response.message)

        response = decorator.findById(2, DEFAULT_LOGGED_IN_USER_ID)
        assertFalse(response.isSuccessful)
        assertEquals("Not found", response.message)

        var listingResponse = decorator.listForRoute("123", mockk())
        assertTrue(listingResponse.isSuccessful)
        assertEquals("pins fetched", listingResponse.message)
        assertEquals(2, listingResponse.resultSize)
        assertEquals(2, listingResponse.data.size)

        listingResponse = decorator.listForRoute("1234", mockk())
        assertFalse(listingResponse.isSuccessful)
        assertEquals("Route Not found", listingResponse.message)
        assertEquals(0, listingResponse.resultSize)
        assertEquals(0, listingResponse.data.size)
    }

    @Test
    fun correctlyHandlesPollService() {

        val successRequest = ListCommentsRequest(1, DEFAULT_LOGGED_IN_USER_ID, mockk())
        val failedRequest = ListCommentsRequest(5, DEFAULT_LOGGED_IN_USER_ID, mockk())

        val service = mockk<PollServiceImpl> {
            every { findById(1, DEFAULT_LOGGED_IN_USER_ID) } returns ServiceResponse(true, "Fetched", mockk())
            every { findById(2, DEFAULT_LOGGED_IN_USER_ID) } throws IllegalStateException("Not found")
            every { listComments(successRequest) } returns ServiceListingResponse(true, "Poll comments fetched",  2, 2, listOf(mockk(), mockk()))
            every { listComments(failedRequest) } throws IllegalStateException("Poll Not found")
        }
        val decorator = ExceptionHandlingPollService(service)

        var response = decorator.findById(1, DEFAULT_LOGGED_IN_USER_ID)
        assertTrue(response.isSuccessful)
        assertEquals("Fetched", response.message)

        response = decorator.findById(2, DEFAULT_LOGGED_IN_USER_ID)
        assertFalse(response.isSuccessful)
        assertEquals("Not found", response.message)

        var listingResponse = decorator.listComments(successRequest)
        assertTrue(listingResponse.isSuccessful)
        assertEquals("Poll comments fetched", listingResponse.message)
        assertEquals(2, listingResponse.resultSize)
        assertEquals(2, listingResponse.data.size)

        listingResponse = decorator.listComments(failedRequest)
        assertFalse(listingResponse.isSuccessful)
        assertEquals("Poll Not found", listingResponse.message)
        assertEquals(0, listingResponse.resultSize)
        assertEquals(0, listingResponse.data.size)
    }

    @Test
    fun correctlyHandlesReportService() {
        val service = mockk<ReportServiceImpl> {
            every { list(any()) } returns ServiceListingResponse(true, "Reports Fetched!", 2, 1, listOf(mockk(), mockk()))
            every { dismiss(1) } returns ServiceResponse(true, "Deleted", null)
            every { dismiss(2) } throws IllegalStateException("Report does not exist")
        }
        val decorator = ExceptionHandlingReportService(service)

        val listResponse = decorator.list(mockk())
        assertTrue(listResponse.isSuccessful)
        assertEquals("Reports Fetched!", listResponse.message)
        assertEquals(2, listResponse.resultSize)
        assertEquals(2, listResponse.data.size)

        val dismissResponse = decorator.dismiss(1)
        assertTrue(dismissResponse.isSuccessful)
        assertEquals("Deleted", dismissResponse.message)
        assertNull(dismissResponse.data)

        val dismissFailedResponse = decorator.dismiss(2)
        assertFalse(dismissFailedResponse.isSuccessful)
        assertEquals("Report does not exist", dismissFailedResponse.message)
    }

    @Test
    fun correctlyHandlesPostService() {
        val successRequest = ListCommentsRequest(1, DEFAULT_LOGGED_IN_USER_ID, mockk())
        val failRequest = ListCommentsRequest(2, DEFAULT_LOGGED_IN_USER_ID, mockk())

        val service = mockk<PostServiceImpl> {
            every { listComments(successRequest) } returns ServiceListingResponse(true, "Comments Fetched!", 2, 1, listOf(mockk(), mockk()))
            every {listComments(failRequest) } throws IllegalStateException("Cant find post")
            every { findById(1, DEFAULT_LOGGED_IN_USER_ID) } returns ServiceResponse(true, "Post fetched", mockk())
            every { findById(2, DEFAULT_LOGGED_IN_USER_ID) } throws IllegalStateException("Post does not exist")
        }
        val decorator = ExceptionHandlingPostService(service)

        val listResponse = decorator.listComments(successRequest)
        assertTrue(listResponse.isSuccessful)
        assertEquals("Comments Fetched!", listResponse.message)
        assertEquals(2, listResponse.resultSize)
        assertEquals(2, listResponse.data.size)

        val listFailedResponse = decorator.listComments(failRequest)
        assertFalse(listFailedResponse.isSuccessful)
        assertEquals("Cant find post", listFailedResponse.message)
        assertTrue(listFailedResponse.data.isEmpty())

        val findByIdResponse = decorator.findById(1, DEFAULT_LOGGED_IN_USER_ID)
        assertTrue(findByIdResponse.isSuccessful)
        assertEquals("Post fetched", findByIdResponse.message)
        assertNotNull(findByIdResponse.data)

        val findByIdFailedResponse = decorator.findById(2, DEFAULT_LOGGED_IN_USER_ID)
        assertFalse(findByIdFailedResponse.isSuccessful)
        assertEquals("Post does not exist", findByIdFailedResponse.message)
    }
}