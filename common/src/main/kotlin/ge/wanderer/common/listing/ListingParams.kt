package ge.wanderer.common.listing


data class ListingParams (
    val batchSize: Int,
    val batchNumber: Int,
    val sortingParams: SortingParams?,
    val filters: List<FilterParam>
)