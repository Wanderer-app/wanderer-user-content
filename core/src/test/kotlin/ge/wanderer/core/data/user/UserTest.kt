package ge.wanderer.core.data.user

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class UserTest {

    @Test
    fun usersWithSameIdAreEqual() {
        val user1 = User(1, "nika", "jamburia")
        val user2 = User(1, "vigaca", "vigaca")
        val user3 = User(2, "vigaca", "vigaca")

        assertEquals(user1, user2)
        assertNotEquals(user2, user3)
    }
}