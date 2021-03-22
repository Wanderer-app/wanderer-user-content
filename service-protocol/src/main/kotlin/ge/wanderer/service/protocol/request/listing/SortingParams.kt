package ge.wanderer.service.protocol.request.listing

data class SortingParams (
    val fieldName: String,
    val sortingDirection: SortingDirection
)