package ge.wanderer.service.protocol.response

data class ServiceListingResponse<T> (
    val isSuccessful: Boolean,
    val message: String,
    val resultSize: Int,
    val batchNumber: Int,
    val data: List<T>
)