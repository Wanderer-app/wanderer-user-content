package ge.wanderer.common.listing

import ge.wanderer.common.listing.SortingDirection

data class SortingParams (
    val fieldName: String,
    val sortingDirection: SortingDirection
)