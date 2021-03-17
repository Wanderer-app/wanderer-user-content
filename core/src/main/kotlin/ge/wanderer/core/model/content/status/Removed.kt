package ge.wanderer.core.model.content.status

import ge.wanderer.core.integration.user.User
import org.joda.time.LocalDateTime
import java.lang.IllegalStateException

class Removed (
    private val createdAt: LocalDateTime,
    private val remover: User
): UserAddedContentStatus {
    override fun createdAt(): LocalDateTime = createdAt
    override fun statusType(): ContentStatusType = ContentStatusType.REMOVED
    override fun remove(onDate: LocalDateTime, remover: User): UserAddedContentStatus = throw IllegalStateException("Content already removed")
    override fun markIrrelevant(onDate: LocalDateTime): UserAddedContentStatus = throw IllegalStateException("Content already removed")

    override fun activate(onDate: LocalDateTime, activator: User): UserAddedContentStatus =
        if ((remover.isAdmin && activator.isAdmin) || remover == activator) {
            Active(onDate, activator)
        } else {
            throw IllegalStateException("You dont have rights to activate this content")
        }

}