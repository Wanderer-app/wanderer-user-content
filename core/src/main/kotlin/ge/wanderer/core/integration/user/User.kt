package ge.wanderer.core.integration.user

data class User (
    val id: String,
    val firstName: String,
    val lastName: String,
    val pinVoteWeight: Int,
    val isAdmin: Boolean,
    val userName: String = "${firstName}.${lastName}"
) {
    override fun equals(other: Any?): Boolean {
        other as User
        return id == other.id
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + firstName.hashCode()
        result = 31 * result + lastName.hashCode()
        return result
    }
}