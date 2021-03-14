package ge.wanderer.core.model.rating

import ge.wanderer.core.model.content.status.UserAddedContentStatus
import ge.wanderer.core.data.user.User
import org.joda.time.LocalDateTime

class Vote(
    private val id: Long,
    private val user: User,
    private val createdAt: LocalDateTime,
    private var status: UserAddedContentStatus,
    private val value: Int,
    private val type: VoteType
): IVote {

    override fun weight(): Int =
        if (type == VoteType.DOWN) {
            value * -1
        } else {
            value
        }

    override fun id(): Long = id
    override fun creator(): User = user
    override fun createdAt(): LocalDateTime = createdAt
    override fun status(): UserAddedContentStatus = status

    override fun remove(onDate: LocalDateTime) {
        status = status.remove(onDate)
    }

    override fun activate(onDate: LocalDateTime) {
        status = status.activate(onDate)
    }
}
