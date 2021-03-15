package ge.wanderer.core.model.discussion.poll

import ge.wanderer.common.amount
import ge.wanderer.core.model.content.UserAddedContent
import ge.wanderer.core.model.content.status.UserAddedContentStatus
import ge.wanderer.core.data.user.User
import ge.wanderer.core.model.content.status.StatusType
import org.joda.time.LocalDateTime
import java.math.RoundingMode

data class PollAnswer (
    private val id: Long,
    private val title: String,
    private val createdAt: LocalDateTime,
    private val creator: User,
    private var status: UserAddedContentStatus,
    private val selectors: MutableSet<User>
): IPollAnswer {

    override fun id(): Long = id
    override fun creator(): User = creator
    override fun createdAt(): LocalDateTime = createdAt
    override fun isActive(): Boolean = status.statusType() == StatusType.ACTIVE
    override fun isRemoved(): Boolean = status.statusType() == StatusType.REMOVED
    override fun statusUpdatedAt(): LocalDateTime = status.createdAt()

    override fun remove(onDate: LocalDateTime) {
        status = status.remove(onDate)
    }
    override fun activate(onDate: LocalDateTime) {
        status = status.activate(onDate)
    }

    override fun selectors() = selectors.toList()

    override fun selectBy(user: User) {
        if (!selectors.add(user)) {
            selectors.remove(user)
        }
    }

    override fun data(totalAnswerers: Int) = PollAnswerData(
        id,
        title,
        selectors.map { it.id },
        amount(selectors().size)
            .multiply(amount(100))
            .divide(amount(totalAnswerers), 2, RoundingMode.HALF_UP))

    override fun numberOfAnswerers(): Int = selectors.size

}