package ge.wanderer.core.model.rating

import ge.wanderer.common.enums.UserContentType
import ge.wanderer.core.model.content.status.UserAddedContentStatus
import ge.wanderer.core.integration.user.User
import ge.wanderer.core.model.content.status.StatusType
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
    override fun isActive(): Boolean = status.statusType() == StatusType.ACTIVE
    override fun isRemoved(): Boolean = status.statusType() == StatusType.REMOVED
    override fun statusUpdatedAt(): LocalDateTime = status.createdAt()
    override fun type(): VoteType = type
    override fun contentType(): UserContentType = UserContentType.VOTE

    override fun remove(onDate: LocalDateTime) {
        status = status.remove(onDate)
    }

    override fun activate(onDate: LocalDateTime) {
        status = status.activate(onDate)
    }

}
