package ge.wanderer.core.model.content.status

import org.joda.time.LocalDateTime
import java.lang.IllegalStateException

class NotRelevant (
    private val createdAt: LocalDateTime
): UserAddedContentStatus {
    override fun createdAt(): LocalDateTime = createdAt
    override fun statusType(): StatusType = StatusType.NOT_RELEVANT
    override fun remove(onDate: LocalDateTime): UserAddedContentStatus = throw IllegalStateException("Cant remove irrelevant content")
    override fun activate(onDate: LocalDateTime): UserAddedContentStatus = throw IllegalStateException("Cant activate irrelevant content")
    override fun markIrrelevant(onDate: LocalDateTime): UserAddedContentStatus = throw IllegalStateException("Already marked as irrelevant")
}