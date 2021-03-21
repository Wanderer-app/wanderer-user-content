package ge.wanderer.core.command.content

import ge.wanderer.common.now
import ge.wanderer.core.*
import ge.wanderer.core.integration.user.UserService
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.lang.IllegalStateException
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class RemoveContentCommandTest {

    @Test
    fun failsIfContentIsAlreadyRemoved() {
        val pin = createTipPin(1, jambura(), now(), mockk(), "123", "Lots of wolfs here")
        pin.remove(now(), jambura())

        val exception = assertThrows<IllegalStateException> {
            RemoveContentCommand(pin, now(), jambura(), mockk()).execute()
        }
        assertEquals("Content already removed", exception.message!!)
    }

    @Test
    fun failsIfContentIsIrrelevant() {
        val pin = createTipPin(1, jambura(), now(), mockk(), "123", "Lots of wolfs here")
        pin.markIrrelevant(now())

        val exception = assertThrows<IllegalStateException> {
            RemoveContentCommand(pin, now(), jambura(), mockk()).execute()
        }
        assertEquals("Cant remove irrelevant content", exception.message!!)
    }

    @Test
    fun notifiesUserServiceIfContentIsRemovedByAdmin() {
        val comment = createNewComment(1, now(), "Some text", kalduna())
        val userService = mockk<UserService> { every { notifyContentStatusChange(comment) } returns Unit }

        val result = RemoveContentCommand(comment, now(), jambura(), userService).execute()
        assertTrue(result.isSuccessful)
        assertTrue(result.returnedModel.isRemoved())
        verify(exactly = 1) { userService.notifyContentStatusChange(comment) }
    }

    @Test
    fun doesNotNotifyUserServiceWhenUserRemovesOwnContent() {
        val comment = createNewComment(1, now(), "Some text", kalduna())
        val userService = mockk<UserService> { every { notifyContentStatusChange(comment) } returns Unit }

        val result = RemoveContentCommand(comment, now(), kalduna(), userService).execute()
        assertTrue(result.isSuccessful)
        assertTrue(result.returnedModel.isRemoved())
        verify(exactly = 0) { userService.notifyContentStatusChange(comment) }
    }

    @Test
    fun failsIfRemoverIsNotCreatorOrAdmin() {
        val comment = createNewPostWithoutFiles(1, kalduna(), "ragaca", now())
        val userService = mockk<UserService> { every { notifyContentStatusChange(comment) } returns Unit }

        val exception = assertThrows<IllegalStateException> {
            RemoveContentCommand(comment, now(), vipiSoxumski(), userService).execute()
        }
        assertEquals("You dont have rights to remove this content", exception.message!!)
    }
}