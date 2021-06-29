package ge.wanderer.service.spring.integration.user.service

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class UserServiceMockImplTest {

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