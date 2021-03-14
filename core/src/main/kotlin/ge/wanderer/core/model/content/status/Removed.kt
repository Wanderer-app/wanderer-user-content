package ge.wanderer.core.model.content.status

import org.joda.time.LocalDateTime
import java.lang.IllegalStateException

class Removed (
    private val createdAt: LocalDateTime
): UserAddedContentStatus {
    override fun createdAt(): LocalDateTime = createdAt
    override fun statusType(): StatusType = StatusType.REMOVED
    override fun remove(onDate: LocalDateTime): UserAddedContentStatus = throw IllegalStateException("Content already removed")
    override fun activate(onDate: LocalDateTime): UserAddedContentStatus = Active(onDate)
    override fun markIrrelevant(onDate: LocalDateTime): UserAddedContentStatus = throw IllegalStateException("Content already removed")
}