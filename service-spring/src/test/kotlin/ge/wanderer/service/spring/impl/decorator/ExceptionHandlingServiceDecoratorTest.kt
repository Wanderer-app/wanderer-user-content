package ge.wanderer.service.spring.impl.decorator

import ge.wanderer.persistence.listing.ListingParams
import ge.wanderer.service.protocol.response.ServiceListingResponse
import ge.wanderer.service.protocol.response.ServiceResponse
import ge.wanderer.service.spring.impl.*
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test
import kotlin.test.*

class ExceptionHandlingServiceDecoratorTest {

    @Test
    fun correctlyHandlesCommentService() {

        val decoratedService = mockk<CommentServiceImpl> {
            every { updateComment(any()) } throws IllegalStateException("Wrong input")
            every { listComments(any(), any()) } throws IllegalStateException("Comment not found")
            every { findById(any()) } throws Exception()
            every { addComment(any()) } returns ServiceResponse(true, "Operation successful", mockk())
        }
        val decorator = ExceptionHandlingCommentService(decoratedService)

        val updateResult = decorator.updateComment(mockk())
        assertFalse(updateResult.isSuccessful)
        assertEquals( "Wrong input", updateResult.message)
        assertNull(updateResult.data)

        val listingResult = decorator.listComments(1, mockk())
        assertFalse(listingResult.isSuccessful)
        assertEquals( "Comment not found", listingResult.message)
        assertTrue(listingResult.data.isEmpty())

        val findByIdResult = decorator.findById(1)
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
            every { getDiscussionForRoute("123", any()) } returns ServiceListingResponse(true, "Discussions fetched", 1, 1, listOf(mockk()))
            every { getDiscussionForRoute("1234", any()) } throws IllegalStateException("Route not found")
            every { getDiscussionForRoute("1235", any()) } throws IllegalStateException()
        }
        val decorator = ExceptionHandlingDiscussionService(service)

        var result = decorator.getDiscussionForRoute("1234", mockk())
        assertFalse(result.isSuccessful)
        assertEquals("Route not found", result.message)
        assertTrue(result.data.isEmpty())

        val listingParams = ListingParams(1, 1, null, listOf())
        result = decorator.getDiscussionForRoute("123", listingParams)
        assertTrue(result.isSuccessful)
        assertEquals("Discussions fetched", result.message)
        assertFalse(result.data.isEmpty())

        result = decorator.getDiscussionForRoute("1235", mockk())
        assertFalse(result.isSuccessful)
        assertTrue(result.message.startsWith("Exception occurred: java.lang.IllegalStateException"))
        assertTrue(result.data.isEmpty())
    }

    @Test
    fun correctlyHandlesPinService() {

        val service = mockk<PinServiceImpl> {
            every { findById(1) } returns ServiceResponse(true, "fetched", mockk())
            every { findById(2) } throws IllegalStateException("Not found")
            every { listForRoute("123", any()) } returns ServiceListingResponse(true, "pins fetched",  2, 2, listOf(mockk(), mockk()))
            every { listForRoute("1234", any()) } throws IllegalStateException("Route Not found")
        }
        val decorator = ExceptionHandlingPinService(service)

        var response = decorator.findById(1)
        assertTrue(response.isSuccessful)
        assertEquals("fetched", response.message)

        response = decorator.findById(2)
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

        val service = mockk<PollServiceImpl> {
            every { findById(1) } returns ServiceResponse(true, "Fetched", mockk())
            every { findById(2) } throws IllegalStateException("Not found")

            every { listComments(1, any()) } returns ServiceListingResponse(true, "Poll comments fetched",  2, 2, listOf(mockk(), mockk()))
            every { listComments(5, any()) } throws IllegalStateException("Poll Not found")
        }
        val decorator = ExceptionHandlingPollService(service)

        var response = decorator.findById(1)
        assertTrue(response.isSuccessful)
        assertEquals("Fetched", response.message)

        response = decorator.findById(2)
        assertFalse(response.isSuccessful)
        assertEquals("Not found", response.message)

        var listingResponse = decorator.listComments(1, mockk())
        assertTrue(listingResponse.isSuccessful)
        assertEquals("Poll comments fetched", listingResponse.message)
        assertEquals(2, listingResponse.resultSize)
        assertEquals(2, listingResponse.data.size)

        listingResponse = decorator.listComments(5, mockk())
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
        val service = mockk<PostServiceImpl> {
            every { listComments(1, any()) } returns ServiceListingResponse(true, "Comments Fetched!", 2, 1, listOf(mockk(), mockk()))
            every { listComments(2, any()) } throws IllegalStateException("Cant find post")
            every { findById(1) } returns ServiceResponse(true, "Post fetched", mockk())
            every { findById(2) } throws IllegalStateException("Post does not exist")
        }
        val decorator = ExceptionHandlingPostService(service)

        val listResponse = decorator.listComments(1, mockk())
        assertTrue(listResponse.isSuccessful)
        assertEquals("Comments Fetched!", listResponse.message)
        assertEquals(2, listResponse.resultSize)
        assertEquals(2, listResponse.data.size)

        val listFailedResponse = decorator.listComments(2, mockk())
        assertFalse(listFailedResponse.isSuccessful)
        assertEquals("Cant find post", listFailedResponse.message)
        assertTrue(listFailedResponse.data.isEmpty())

        val findByIdResponse = decorator.findById(1)
        assertTrue(findByIdResponse.isSuccessful)
        assertEquals("Post fetched", findByIdResponse.message)
        assertNotNull(findByIdResponse.data)

        val findByIdFailedResponse = decorator.findById(2)
        assertFalse(findByIdFailedResponse.isSuccessful)
        assertEquals("Post does not exist", findByIdFailedResponse.message)
    }
}