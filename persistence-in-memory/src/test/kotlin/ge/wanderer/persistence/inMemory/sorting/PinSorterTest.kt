package ge.wanderer.persistence.inMemory.sorting

import ge.wanderer.common.dateTime
import ge.wanderer.common.listing.SortingDirection.*
import ge.wanderer.common.listing.SortingParams
import ge.wanderer.core.model.map.IPin
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.lang.IllegalStateException
import kotlin.test.assertEquals

class PinSorterTest {

    private val pin1 = mockk<IPin> {
        every { rating() } returns 2
        every { createdAt() } returns dateTime("2021-04-14T12:11:11")
    }
    private val pin2 = mockk<IPin> {
        every { rating() } returns 1
        every { createdAt() } returns dateTime("2021-04-14T12:11:13")
    }
    private val pin3 = mockk<IPin> {
        every { rating() } returns 3
        every { createdAt() } returns dateTime("2021-04-14T12:11:12")
    }
    val pins = listOf(pin1, pin2, pin3)

    @Test
    fun sortsPinsByRatingCorrectly() {
        var sortedPins = PinSorter().sort(pins.asSequence(), SortingParams("rating", DESCENDING)).toList()
        assertEquals(sortedPins.first().rating(), 3)
        assertEquals(sortedPins.last().rating(), 1)

        sortedPins = PinSorter().sort(pins.asSequence(), SortingParams("rating", ASCENDING)).toList()
        assertEquals(sortedPins.first().rating(), 1)
        assertEquals(sortedPins.last().rating(), 3)
    }

    @Test
    fun sortsPinsByCreateTimeCorrectly() {
        var sortedPins = PinSorter().sort(pins.asSequence(), SortingParams("createdAt", DESCENDING)).toList()
        assertEquals(sortedPins.first().createdAt(), dateTime("2021-04-14T12:11:13"))
        assertEquals(sortedPins.last().createdAt(), dateTime("2021-04-14T12:11:11"))

        sortedPins = PinSorter().sort(pins.asSequence(), SortingParams("createdAt", ASCENDING)).toList()
        assertEquals(sortedPins.first().createdAt(), dateTime("2021-04-14T12:11:11"))
        assertEquals(sortedPins.last().createdAt(), dateTime("2021-04-14T12:11:13"))
    }

    @Test
    fun failsWhenFieldIsNotSupportedBySorter() {
        val exception = assertThrows<IllegalStateException> {
            PinSorter().sort(pins.asSequence(), SortingParams("some field", DESCENDING)).toList()
        }
        assertEquals("Cant sort by some field", exception.message!!)
    }

}