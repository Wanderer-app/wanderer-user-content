package ge.wanderer.core.model.content

import ge.wanderer.core.model.user.User

class Vote(
    private val user: User,
    private val value: Int,
    private val type: VoteType
) {
    fun value(): Int =
        if (type == VoteType.DOWN) {
            value * -1
        } else {
            value
        }
}

enum class VoteType { UP, DOWN }