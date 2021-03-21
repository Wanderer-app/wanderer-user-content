package ge.wanderer.core.command.comment

import ge.wanderer.common.now
import ge.wanderer.core.model.UpdateCommentData
import ge.wanderer.core.createNewComment
import ge.wanderer.core.jambura
import ge.wanderer.core.patata
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.lang.IllegalStateException
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class UpdateCommentCommandTest {

    @Test
    fun failsIfUpdaterIsNotAuthor() {
        val comment = createNewComment(1, now(), "Some comment", jambura())

        val exception = assertThrows<IllegalStateException> {
            UpdateCommentCommand(comment, UpdateCommentData("Some comment!"), patata()).execute()
        }
        assertEquals("You can't update this comment", exception.message!!)
    }

    @Test
    fun updatesCommentCorrectly() {
        val comment = createNewComment(1, now(), "Some comment", jambura())

        val result = UpdateCommentCommand(comment, UpdateCommentData("Updated comment"), jambura()).execute()
        assertTrue(result.isSuccessful)
        assertEquals("Comment updated", result.message)
        assertEquals("Updated comment", result.returnedModel.text())
    }
}