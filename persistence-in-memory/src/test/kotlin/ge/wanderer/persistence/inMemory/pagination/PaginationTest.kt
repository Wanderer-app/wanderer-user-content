package ge.wanderer.persistence.inMemory.pagination

import ge.wanderer.common.listing.ListingParams
import org.junit.jupiter.api.Test
import kotlin.test.assertTrue

class PaginationTest {

    @Test
    fun correctlyPaginatesAList() {

        // each line represents 5-sized page
        val list = listOf(
            1, 2, 3, 4, 5,
            6, 7, 8, 9, 10,
            11, 12
        )

        // page 1
        assertTrue(
            list.paginate(ListingParams(5, 1, null, listOf()))
                .all { listOf( 1, 2, 3, 4, 5).contains(it) }
        )

        // page 2
        assertTrue(
            list.paginate(ListingParams(5, 2, null, listOf()))
                .all { listOf(6, 7, 8, 9, 10).contains(it) }
        )

        // page 3
        assertTrue(
            list.paginate(ListingParams(5, 3, null, listOf()))
                .all { listOf(11, 12).contains(it) }
        )

        // page 4
        assertTrue(list.paginate(ListingParams(5, 4, null, listOf())).isEmpty())

        // page 1 of size 12
        assertTrue(
            list.paginate(ListingParams(12, 1, null, listOf()))
                .all { list.contains(it) }
        )

        // page 1 of size 12
        assertTrue(
            list.paginate(ListingParams(13, 1, null, listOf()))
                .all { list.contains(it) }
        )

        // page 2 of size 12
        assertTrue(list.paginate(ListingParams(12, 2, null, listOf())).isEmpty())
    }
}