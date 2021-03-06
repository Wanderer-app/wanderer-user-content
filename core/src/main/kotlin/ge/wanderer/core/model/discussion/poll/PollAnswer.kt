package ge.wanderer.core.model.discussion.poll

import ge.wanderer.common.functions.amount
import ge.wanderer.common.enums.UserContentType
import ge.wanderer.common.functions.percentOf
import ge.wanderer.core.integration.user.User
import ge.wanderer.core.model.content.status.ContentStatusType
import ge.wanderer.core.model.content.status.UserAddedContentStatus
import org.joda.time.LocalDateTime

data class PollAnswer (
    private val id: Long,
    private val title: String,
    private val createdAt: LocalDateTime,
    private val creator: User,
    private var status: UserAddedContentStatus,
    private val selectors: MutableSet<User> = mutableSetOf()
): IPollAnswer {

    override fun id(): Long = id
    override fun creator(): User = creator
    override fun createdAt(): LocalDateTime = createdAt
    override fun isActive(): Boolean = status.statusType() == ContentStatusType.ACTIVE
    override fun isRemoved(): Boolean = status.statusType() == ContentStatusType.REMOVED
    override fun statusUpdatedAt(): LocalDateTime = status.createdAt()
    override fun contentType(): UserContentType = UserContentType.POLL_ANSWER
    override fun selectors() = selectors.toList()
    override fun numberOfAnswerers(): Int = selectors.size
    override fun text(): String = title

    override fun remove(onDate: LocalDateTime, remover: User) {
        status = status.remove(onDate, remover)
    }
    override fun activate(onDate: LocalDateTime, activator: User) {
        status = status.activate(onDate, activator)
    }

    override fun selectBy(user: User) {
        if (!selectors.add(user)) {
            selectors.remove(user)
        }
    }

    override fun data(totalAnswerers: Int) =
        PollAnswerData(
            id,
            title,
            selectors.map { it.id },
            amount(selectors().size).percentOf(amount(totalAnswerers))
        )

}