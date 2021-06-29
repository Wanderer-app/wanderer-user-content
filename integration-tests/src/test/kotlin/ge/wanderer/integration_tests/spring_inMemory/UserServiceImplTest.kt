package ge.wanderer.integration_tests.spring_inMemory

import ge.wanderer.integration_tests.SpringServiceWithInMemoryPersistenceApp
import ge.wanderer.service.spring.integration.user.service.UserServiceImpl
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestPropertySource
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@SpringBootTest(classes = [SpringServiceWithInMemoryPersistenceApp::class])
@TestPropertySource("classpath:service-users-integration-test.properties")
class UserServiceImplTest(
    @Autowired private val userService: UserServiceImpl
) {

    @Test
    fun getsUserById() {

        val user = userService.findUserById("85fa0681-b7bd-4ee3-b5b5-eb2672181ae2")
        assertEquals("85fa0681-b7bd-4ee3-b5b5-eb2672181ae2", user.id)
        assertEquals("Nika", user.firstName)
        assertEquals("Patatishvili", user.lastName)
        assertFalse(user.isAdmin)

        val user2 = userService.findUserById("5760b116-6aab-4f04-b8be-650e27a85d09")
        assertEquals("5760b116-6aab-4f04-b8be-650e27a85d09", user2.id)
        assertEquals("Nika", user2.firstName)
        assertEquals("Jamburia", user2.lastName)
        assertTrue(user2.isAdmin)
    }
}