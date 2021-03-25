package ge.wanderer.service.spring.impl

import ge.wanderer.common.enums.UserContentType
import ge.wanderer.common.listing.ListingParams
import ge.wanderer.common.now
import ge.wanderer.core.repository.DiscussionRepository
import ge.wanderer.service.spring.*
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.lang.IllegalStateException
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class DiscussionServiceImplTest {

    @Test
    fun correctlyFetchesDiscussionsByRoute() {
        val repository = mockk<DiscussionRepository> {
            every { listForRoute("123", any()) } returns listOf(
                createNewPostWithoutFiles(1, patata(), "Some text", now()),
                pollWithAnswers(1, jambura(), now(), "123", "Some question", setOf("Answer 1", "Answer 2", "Answer 3")),
                createNewPostWithoutFiles(2, jangula(), "Some text 2", now())
            )
        }

        val service = DiscussionServiceImpl(repository, CommentPreviewProvider(3))
        val listingParams = ListingParams(5, 1, null, listOf())

        val response = service.getDiscussionForRoute("123", listingParams)
        assertTrue(response.isSuccessful)
        assertEquals("Discussions fetched", response.message)
        assertEquals(3, response.resultSize)
        assertEquals(1, response.batchNumber)

        val discussion = response.data
        assertEquals(3, discussion.size)

        assertEquals("Some text", discussion[0].content)
        assertEquals(patata(), discussion[0].creator)
        assertTrue(discussion[0].commentsPreview.isEmpty())
        assertEquals(UserContentType.POST, discussion[0].type)
        assertNotNull(discussion[0].ratingData)

        val expectedPollJson = "{\"question\":\"Some question\",\"answers\":[{\"answerId\":0,\"title\":\"Answer 1\",\"answererIds\":[],\"percentage\":0.00},{\"answerId\":0,\"title\":\"Answer 2\",\"answererIds\":[],\"percentage\":0.00},{\"answerId\":0,\"title\":\"Answer 3\",\"answererIds\":[],\"percentage\":0.00}]}"
        assertEquals(expectedPollJson, discussion[1].content)
        assertEquals(jambura(), discussion[1].creator)
        assertTrue(discussion[1].commentsPreview.isEmpty())
        assertEquals(UserContentType.POLL, discussion[1].type)
        assertNull(discussion[1].ratingData)

        assertEquals("Some text 2", discussion[2].content)
        assertEquals(jangula(), discussion[2].creator)
        assertTrue(discussion[2].commentsPreview.isEmpty())
        assertEquals(UserContentType.POST, discussion[2].type)
        assertNotNull(discussion[2].ratingData)

    }

    @Test
    fun failsIfErrorOccursDuringFetching() {
        val repository = mockk<DiscussionRepository> {
            every { listForRoute("123", any()) } throws IllegalStateException("Route does not exist")
        }
        val service = DiscussionServiceImpl(repository, CommentPreviewProvider(3))

        val exception = assertThrows<IllegalStateException> { service.getDiscussionForRoute("123", mockk()) }
        assertEquals("Route does not exist", exception.message!!)
    }
}