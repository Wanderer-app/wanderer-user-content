package ge.wanderer.core.model.discussion.poll

import ge.wanderer.common.amount
import ge.wanderer.core.model.content.UserAddedContent
import ge.wanderer.core.model.content.status.UserAddedContentStatus
import ge.wanderer.core.data.user.User
import org.joda.time.LocalDateTime

data class PollAnswer (
    private val id: Long,
    private val title: String,
    private val createdAt: LocalDateTime,
    private val creator: User,
    private var status: UserAddedContentStatus,
    private val selectors: MutableSet<User>
): UserAddedContent {

    override fun id(): Long = id
    override fun creator(): User = creator
    override fun createdAt(): LocalDateTime = createdAt
    override fun status(): UserAddedContentStatus = status
    override fun remove(onDate: LocalDateTime) {
        status = status.remove(onDate)
    }
    override fun activate(onDate: LocalDateTime) {
        status = status.activate(onDate)
    }

    fun selectors() = selectors

    fun selectBy(user: User) {
        if (!selectors.add(user)) {
            selectors.remove(user)
        }
    }

    fun data(totalAnswerers: Int) = PollAnswerData(
        id,
        title,
        selectors.map { it.id },
        amount(selectors().size)
            .multiply(amount(totalAnswerers))
            .divide(amount(100)))

}