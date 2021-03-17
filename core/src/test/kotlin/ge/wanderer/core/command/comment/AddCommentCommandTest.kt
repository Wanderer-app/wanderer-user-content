package ge.wanderer.core.command.comment

import ge.wanderer.common.now
import ge.wanderer.core.model.createNewComment
import ge.wanderer.core.model.createNewPostWithoutFiles
import ge.wanderer.core.model.createPoll
import ge.wanderer.core.model.createTipPin
import io.mockk.mockk
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class AddCommentCommandTest {

    @Test
    fun correctlyAddsCommentsToOtherComments() {

        val comment = createNewComment(1, now(), "Some text", mockk())
        val reply = createNewComment(2, now(), "Reply to some text", mockk())
        val result = AddCommentCommand(reply, comment).execute()

        assertTrue(result.isSuccessful)
        assertEquals("Comment added", result.message)

        val returnedContentComments = result.returnedModel.comments()
        assertEquals(1, returnedContentComments.size)
        assertEquals("Reply to some text", returnedContentComments.first().text())
    }

    @Test
    fun correctlyAddsCommentsToPins() {

        val pin = createTipPin(1, mockk(), now(), mockk(), "123", "Teeext")
        val comment = createNewComment(1, now(), "Some text", mockk())
        val result = AddCommentCommand(comment, pin).execute()

        assertTrue(result.isSuccessful)
        assertEquals("Comment added", result.message)

        val returnedContentComments = result.returnedModel.comments()
        assertEquals(1, returnedContentComments.size)
        assertEquals("Some text", returnedContentComments.first().text())
    }

    @Test
    fun correctlyAddsCommentToDiscussionElements() {

        val post = createNewPostWithoutFiles(1, mockk(), "Teeext", now())
        val comment = createNewComment(1, now(), "Some text", mockk())
        var result = AddCommentCommand(comment, post).execute()

        assertTrue(result.isSuccessful)
        assertEquals("Comment added", result.message)
        assertEquals(1,result.returnedModel.comments().size)
        assertEquals("Some text", result.returnedModel.comments().first().text())

        val poll = createPoll(1, mockk(), now(), "123", "Some question", mutableSetOf())
        result = AddCommentCommand(comment, poll).execute()

        assertTrue(result.isSuccessful)
        assertEquals("Comment added", result.message)

        val returnedContentComments = result.returnedModel.comments()
        assertEquals(1, returnedContentComments.size)
        assertEquals("Some text", returnedContentComments.first().text())
    }
}