package ge.wanderer.persistence.inMemory.repository

import ge.wanderer.common.constants.TRANSIENT_ID
import ge.wanderer.common.enums.PinType
import ge.wanderer.common.listing.ListingParams
import ge.wanderer.common.listing.SortingDirection
import ge.wanderer.common.listing.SortingParams
import ge.wanderer.common.map.LatLng
import ge.wanderer.common.now
import ge.wanderer.core.integration.user.UserService
import ge.wanderer.core.model.comment.Comment
import ge.wanderer.core.model.content.status.Active
import ge.wanderer.persistence.inMemory.WandererInMemoryPersistenceApplication
import ge.wanderer.persistence.inMemory.support.DEFAULT_LISTING_PARAMS
import ge.wanderer.persistence.inMemory.support.createPin
import ge.wanderer.persistence.inMemory.support.jambura
import io.mockk.mockk
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue

@SpringBootTest(classes = [WandererInMemoryPersistenceApplication::class])
class PinRepositoryImplTest(
    @Autowired private val pinRepositoryImpl: PinRepositoryImpl,
    @Autowired private val commentRepositoryImpl: CommentRepositoryImpl,
    @Autowired private val userService: UserService
) {

    @Test
    fun isCorrectlyInitializedAndCanBeWorkedWith() {
        assertTrue(pinRepositoryImpl.list(DEFAULT_LISTING_PARAMS).isNotEmpty())
        val pin = pinRepositoryImpl.findById(3)

        val user = userService.findUserById("90d36b5f-e060-4f67-a4a2-c6d06ee76b04")
        pin.addComment(Comment(TRANSIENT_ID, user, now(), "New comment", Active(now(), user)))

        val comments = commentRepositoryImpl.listActiveFor(pin, DEFAULT_LISTING_PARAMS)
        assertEquals(2, comments.size)
        assertEquals("New comment", comments.last().text())
    }

    @Test
    fun persistsNewPins() {
        val pin = createPin(TRANSIENT_ID, PinType.TIP, jambura(), now(), LatLng(5f, 5f), "1231", "Some text")
        pinRepositoryImpl.persist(pin)

        val persistedPin = pinRepositoryImpl.findById(6)
        assertEquals("Some text", persistedPin.content().text)
        assertNotEquals(TRANSIENT_ID, persistedPin.id())
    }

    @Test
    fun deletesPin() {
        pinRepositoryImpl.delete(1)
        val exception = assertThrows<IllegalStateException> { pinRepositoryImpl.findById(1) }
        assertEquals("Not found", exception.message!!)
    }

    @Test
    fun listsPinsByRoute() {
        val pins = pinRepositoryImpl.listForRoute("TB201301", DEFAULT_LISTING_PARAMS)
        assertEquals(5, pins.size)
        assertTrue(pins.all { it.routeCode() == "TB201301" })
    }
}