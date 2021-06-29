package ge.wanderer.service.spring.integration.user.service

import ge.wanderer.common.enums.PinType
import ge.wanderer.common.map.LatLng
import ge.wanderer.common.now
import ge.wanderer.core.integration.user.User
import ge.wanderer.service.spring.integration.user.api.*
import ge.wanderer.service.spring.test_support.createPin
import ge.wanderer.service.spring.test_support.createUpVote
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.lang.IllegalStateException
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class UserServiceImplTest {

    private val apiUrl = "http://127.0.0.1:5000/api/v1/"

    private val TEST_MOCKED = true
    private val apiClient = if (TEST_MOCKED) {
        mockedUserApiClient()
    } else {
        realUserApiClient(apiUrl)
    }

    @Test
    fun getsUserById() {

        val service = UserServiceImpl(apiClient)

        val user = service.findUserById("85fa0681-b7bd-4ee3-b5b5-eb2672181ae2")
        assertEquals("85fa0681-b7bd-4ee3-b5b5-eb2672181ae2", user.id)
        assertEquals("Nika", user.firstName)
        assertEquals("Patatishvili", user.lastName)
        assertFalse(user.isAdmin)

        val user2 = service.findUserById("5760b116-6aab-4f04-b8be-650e27a85d09")
        assertEquals("5760b116-6aab-4f04-b8be-650e27a85d09", user2.id)
        assertEquals("Nika", user2.firstName)
        assertEquals("Jamburia", user2.lastName)
        assertTrue(user2.isAdmin)
    }

    @Test
    fun throwsExceptionOnNotFound() {

        val service = UserServiceImpl(apiClient)

        val exception = assertThrows<IllegalStateException> { service.findUserById("1") }
        assertEquals("404 User not found", exception.message!!)

    }

    @Test
    fun throwsExceptionIfIdNotProvided() {
        val service = UserServiceImpl(apiClient)

        val exception = assertThrows<IllegalStateException> { service.findUserById("") }
        assertEquals("400 Id should be provided", exception.message!!)
    }

    @Test
    fun sendsNotificationOnContentRated() {
        val service = UserServiceImpl(apiClient)

        val user = User("5760b116-6aab-4f04-b8be-650e27a85d09", "Nika", "Jamburia", 5, true)
        val pin = createPin(1, PinType.DANGER, user, now(), LatLng(1f, 1f), "TB201301", "some text")
        val vote = createUpVote(1, user, now(), 1)
        service.usersContentWasRated(pin, vote)

        assertTrue(true)
    }

    @Test
    fun getsAdminUser() {
        val service = UserServiceImpl(apiClient)

        val user = service.getAdministrationUser()
        assertTrue(user.isAdmin)
    }
}