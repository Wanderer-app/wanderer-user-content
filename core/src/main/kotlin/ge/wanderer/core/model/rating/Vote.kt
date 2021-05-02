package ge.wanderer.core.model.rating

import ge.wanderer.common.enums.UserContentType
import ge.wanderer.common.enums.VoteType
import ge.wanderer.core.integration.user.User
import ge.wanderer.core.model.content.status.ContentStatusType
import ge.wanderer.core.model.content.status.UserAddedContentStatus
import org.joda.time.LocalDateTime

class Vote(
    private val id: Long,
    private val creator: User,
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
    override fun creator(): User = creator
    override fun createdAt(): LocalDateTime = createdAt
    override fun isActive(): Boolean = status.statusType() == ContentStatusType.ACTIVE
    override fun isRemoved(): Boolean = status.statusType() == ContentStatusType.REMOVED
    override fun statusUpdatedAt(): LocalDateTime = status.createdAt()
    override fun type(): VoteType = type
    override fun contentType(): UserContentType = UserContentType.VOTE

    override fun remove(onDate: LocalDateTime, remover: User) {
        if (remover == creator) {
            status = status.remove(onDate, remover)
        } else {
            throw IllegalStateException("Vote can only be removed by it's creator")
        }
    }
    override fun activate(onDate: LocalDateTime, activator: User) {
        status = status.activate(onDate, activator)
    }

}
