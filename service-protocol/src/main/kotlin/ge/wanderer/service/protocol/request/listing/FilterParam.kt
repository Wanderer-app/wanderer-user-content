package ge.wanderer.service.protocol.request.listing

data class FilterParam (
    val fieldName: String,
    val operation: FilterOperation,
    val compareValue: String
)
enum class FilterOperation { IS, IS_NOT, IS_MORE_THEN, IS_LESS_THEN }