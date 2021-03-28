package ge.wanderer.core.command.comment

import ge.wanderer.common.now
import ge.wanderer.core.createNewComment
import ge.wanderer.core.createNewPostWithoutFiles
import ge.wanderer.core.createPoll
import ge.wanderer.core.createTipPin
import ge.wanderer.core.integration.user.UserService
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class AddCommentCommandTest {

    @Test
    fun correctlyAddsCommentsToOtherComments() {

        val userService = mockk<UserService> { every { notifyContentWasCommented(any(), any()) } returns Unit }
        val comment = createNewComment(1, now(), "Some text", mockk())
        val result = AddCommentCommand(
            "Reply to some text",
            mockk(),
            now(),
            comment,
            userService
        ).execute()

        assertTrue(result.isSuccessful)
        assertEquals("Comment added", result.message)

        val returnedContentComments = result.returnedModel.comments()
        assertEquals(1, returnedContentComments.size)
        assertEquals("Reply to some text", returnedContentComments.first().text())
        verify(exactly = 1) { userService.notifyContentWasCommented(any(), any()) }
    }

    @Test
    fun correctlyAddsCommentsToPins() {

        val pin = createTipPin(1, mockk(), now(), mockk(), "123", "Teeext")
        val userService = mockk<UserService> { every { notifyContentWasCommented(any(), any()) } returns Unit }
        val result = AddCommentCommand("Some text", mockk(), now(), pin, userService).execute()

        assertTrue(result.isSuccessful)
        assertEquals("Comment added", result.message)

        val returnedContentComments = result.returnedModel.comments()
        assertEquals(1, returnedContentComments.size)
        assertEquals("Some text", returnedContentComments.first().text())
        verify(exactly = 1) { userService.notifyContentWasCommented(any(), any()) }
    }

    @Test
    fun correctlyAddsCommentToDiscussionElements() {

        val userService = mockk<UserService> { every { notifyContentWasCommented(any(), any()) } returns Unit }
        val post = createNewPostWithoutFiles(1, mockk(), "Teeext", now())
        val postResult = AddCommentCommand("Some text", mockk(), now(), post, userService).execute()

        assertTrue(postResult.isSuccessful)
        assertEquals("Comment added", postResult.message)
        assertEquals(1,postResult.returnedModel.comments().size)
        assertEquals("Some text", postResult.returnedModel.comments().first().text())
        verify(exactly = 1) { userService.notifyContentWasCommented(any(), any()) }

        val poll = createPoll(1, mockk(), now(), "123", "Some question", mutableSetOf())
        val pollResult = AddCommentCommand("Some text", mockk(), now(), poll, userService).execute()

        assertTrue(pollResult.isSuccessful)
        assertEquals("Comment added", pollResult.message)

        val returnedContentComments = pollResult.returnedModel.comments()
        assertEquals(1, returnedContentComments.size)
        assertEquals("Some text", returnedContentComments.first().text())
        verify(exactly = 2) { userService.notifyContentWasCommented(any(), any()) }
    }
}