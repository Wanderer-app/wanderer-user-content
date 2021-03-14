package ge.wanderer.core.model.content.status

import org.joda.time.LocalDateTime
import java.lang.IllegalStateException

class Active (
    private val createdAt: LocalDateTime
): UserAddedContentStatus {
    override fun createdAt(): LocalDateTime = createdAt
    override fun statusType(): StatusType = StatusType.ACTIVE
    override fun remove(onDate: LocalDateTime): UserAddedContentStatus = Removed(onDate)
    override fun activate(onDate: LocalDateTime): UserAddedContentStatus = throw IllegalStateException("Content already active")
    override fun markIrrelevant(onDate: LocalDateTime): UserAddedContentStatus = NotRelevant(onDate)
}