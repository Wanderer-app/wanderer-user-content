package ge.wanderer.service.protocol.response

class ServiceResponse<T> (
    val isSuccessful: Boolean,
    val message: String,
    val data: T?
)