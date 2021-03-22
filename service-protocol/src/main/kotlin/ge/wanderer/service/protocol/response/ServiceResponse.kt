package ge.wanderer.service.protocol.response

class ServiceResponse<T> (
    val isSuccessful: String,
    val message: String,
    val data: T
)