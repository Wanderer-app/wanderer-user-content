package ge.wanderer.common.listing

import ge.wanderer.common.listing.FilterParam
import ge.wanderer.common.listing.SortingParams

data class ListingRequest (
    val batchSize: Int,
    val batchNumber: Int,
    val sortingParams: SortingParams?,
    val filters: List<FilterParam>
)