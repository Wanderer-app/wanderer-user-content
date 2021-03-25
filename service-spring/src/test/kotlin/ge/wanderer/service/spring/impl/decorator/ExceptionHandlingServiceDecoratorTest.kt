package ge.wanderer.service.spring.impl.decorator

import ge.wanderer.common.listing.ListingParams
import ge.wanderer.service.protocol.response.ServiceListingResponse
import ge.wanderer.service.protocol.response.ServiceResponse
import ge.wanderer.service.spring.impl.CommentServiceImpl
import ge.wanderer.service.spring.impl.DiscussionServiceImpl
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test
import java.lang.Exception
import java.lang.IllegalStateException
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
}