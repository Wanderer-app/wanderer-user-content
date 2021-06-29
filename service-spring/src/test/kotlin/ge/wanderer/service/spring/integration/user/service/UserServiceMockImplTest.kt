package ge.wanderer.service.spring.integration.user.service

import ge.wanderer.service.spring.integration.user.service.UserServiceMockImpl
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class UserServiceMockImplTest {

    @Test
    fun correctlyFindsById() {
        val service = UserServiceMockImpl()

        assertEquals("1", service.findUserById("1").id)
        assertEquals("2", service.findUserById("2").id)
        assertEquals("3", service.findUserById("3").id)
        assertEquals("4", service.findUserById("4").id)
        assertEquals("5", service.findUserById("5").id)
        assertEquals("6", service.findUserById("6").id)
        assertEquals("7", service.findUserById("7").id)
        assertEquals("8", service.findUserById("8").id)
        assertEquals("9", service.findUserById("9").id)
        assertEquals("10", service.findUserById("10").id)
    }

    @Test
    fun correctlyReturnsMainAdmin() {
        val admin = UserServiceMockImpl().getAdministrationUser()
        assertEquals("Nika", admin.firstName)
        assertEquals("Jamburia", admin.lastName)
        assertTrue(admin.isAdmin)
    }

    @Test
    fun failsIfUserDoesNotExist() {
        val exception = assertThrows<IllegalStateException> { UserServiceMockImpl().findUserById("11") }
        assertEquals("User with id 11 not found!", exception.message!!)
    }
}