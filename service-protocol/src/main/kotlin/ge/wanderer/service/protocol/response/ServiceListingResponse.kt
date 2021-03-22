package ge.wanderer.service.protocol.response

data class ServiceListingResponse<T> (
    val isSuccessful: String,
    val message: String,
    val resultSize: String,
    val batchNumber: Int,
    val data: List<T>
)