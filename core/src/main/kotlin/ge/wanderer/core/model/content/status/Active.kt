package ge.wanderer.core.model.content.status

import ge.wanderer.core.integration.user.User
import org.joda.time.LocalDateTime

class Active (
    private val createdAt: LocalDateTime,
    private val creator: User
): UserAddedContentStatus {
    override fun createdAt(): LocalDateTime = createdAt
    override fun statusType(): ContentStatusType = ContentStatusType.ACTIVE
    override fun activate(onDate: LocalDateTime, activator: User): UserAddedContentStatus = throw IllegalStateException("Content already active")
    override fun markIrrelevant(onDate: LocalDateTime): UserAddedContentStatus = NotRelevant(onDate)

    override fun remove(onDate: LocalDateTime, remover: User): UserAddedContentStatus {
        check(remover == creator || remover.isAdmin) { "You dont have rights to remove this content" }
        return Removed(onDate, remover)
    }
}