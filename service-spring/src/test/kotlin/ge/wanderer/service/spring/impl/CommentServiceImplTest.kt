package ge.wanderer.service.spring.impl

import ge.wanderer.common.now
import ge.wanderer.core.configuration.ReportingConfiguration
import ge.wanderer.core.integration.user.UserService
import ge.wanderer.core.repository.CommentRepository
import ge.wanderer.service.protocol.request.UpdateCommentRequest
import ge.wanderer.service.spring.*
import ge.wanderer.service.spring.command.CommandProvider
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

    private val commentRepository = mockk<CommentRepository> {
        every { findById(1) } returns comment1
        every { findById(2) } returns comment2
        every { findById(3) } returns comment3
        every { findById(4) } returns comment4
    }
    private val userService = mockk<UserService> {
        every { findUserById(1) } returns jambura()
        every { findUserById(2) } returns patata()
        every { findUserById(3) } returns jangula()
        every { findUserById(5) } returns kalduna()
        every { notifyContentStatusChange(any()) } returns Unit
        every { notifyAdministrationAboutReport(any()) } returns Unit
        every { getAdministrationUser() } returns jambura()
    }

    private val commandProvider = CommandProvider()
    private val commentPreviewProvider = CommentPreviewProvider(3)
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
        assertEquals(kalduna(), commentData.author)
        assertEquals("sadaa chemi 1000 maneti?", commentData.text)

        verify(exactly = 1) { commentRepository.findById(3) }
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
        assertEquals(patata(), response.data!!.author)
        verify(exactly = 1) { commentRepository.findById(3) }
    }
}