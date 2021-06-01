package ge.wanderer.service.protocol.data

data class UserData (
    val id: Long,
    val firstName: String,
    val lastName: String,
    val isAdmin: Boolean
)