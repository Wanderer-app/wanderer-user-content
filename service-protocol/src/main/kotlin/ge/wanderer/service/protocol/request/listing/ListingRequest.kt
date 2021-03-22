package ge.wanderer.service.protocol.request.listing

data class ListingRequest (
    val batchSize: Int,
    val batchNumber: Int,
    val sortingParams: SortingParams,
    val filters: List<FilterParam>
)