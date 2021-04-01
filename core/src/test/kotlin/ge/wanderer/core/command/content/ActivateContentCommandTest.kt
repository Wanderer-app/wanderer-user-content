package ge.wanderer.core.command.content

import ge.wanderer.common.now
import ge.wanderer.core.createNewPostWithoutFiles
import ge.wanderer.core.integration.user.User
import ge.wanderer.core.integration.user.UserService
import ge.wanderer.core.jambura
import ge.wanderer.core.kalduna
import ge.wanderer.core.model.discussion.post.IPost
import ge.wanderer.core.vipiSoxumski
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ActivateContentCommandTest {

    @Test
    fun contentRemovedByUserCanOnlyBeActivatedByUser() {
        val userService: UserService = mockk()
        val author = vipiSoxumski()
        val post = postRemovedByAuthor(author)
        val result = ActivateContentCommand(author, post, now(), userService).execute()

        assertTrue(result.isSuccessful)
        assertEquals("POST activated successfully!", result.message)
        assertTrue(result.returnedModel.isActive())

        assertThrows<IllegalStateException>("You dont have rights to activate this content") {
            ActivateContentCommand(jambura(), post, now(), userService).execute()
        }
        assertThrows<IllegalStateException>("You dont have rights to activate this content") {
            ActivateContentCommand(kalduna(), post, now(), userService).execute()
        }
    }

    @Test
    fun contentRemovedByAdminCanOnlyBeActivatedByAdmin() {
        val author = vipiSoxumski()
        var post = postRemovedByAdmin(author)
        val userService: UserService = mockk { every { notifyContentStatusChange(post) } returns Unit }
        val result = ActivateContentCommand(jambura(), post, now(), userService).execute()

        assertTrue(result.isSuccessful)
        assertEquals("POST activated successfully!", result.message)
        assertTrue(result.returnedModel.isActive())

        post = postRemovedByAdmin(author)
        val exception = assertThrows<IllegalStateException> {
            ActivateContentCommand(author, post, now(), userService).execute()
        }
        assertEquals("You dont have rights to activate this content", exception.message!!)
    }

    @Test
    fun notifiesUserServiceWhenActivatedByAdmin() {
        val post = postRemovedByAdmin(kalduna())
        val userService: UserService = mockk { every { notifyContentStatusChange(post) } returns Unit }

        ActivateContentCommand(jambura(), post, now(), userService).execute()
        verify(exactly = 1) { userService.notifyContentStatusChange(post) }
    }

    @Test
    fun failsIfContentAlreadyActive() {
        val post = createNewPostWithoutFiles(1, kalduna(), "Some text", now())
        val exception = assertThrows<IllegalStateException> {
            ActivateContentCommand(kalduna(), post, now(), mockk()).execute()
        }
        assertEquals("Content already active", exception.message!!)
    }

    private fun postRemovedByAuthor(author: User): IPost {
        val post = createNewPostWithoutFiles(1, author, "Some text", now())
        post.remove(now(), author)
        return post
    }

    private fun postRemovedByAdmin(author: User): IPost {
        val post = createNewPostWithoutFiles(1, author, "Some text", now())
        post.remove(now(), jambura())
        return post
    }
}