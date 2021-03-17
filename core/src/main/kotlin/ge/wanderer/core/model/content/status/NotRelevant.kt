package ge.wanderer.core.model.content.status

import ge.wanderer.core.integration.user.User
import org.joda.time.LocalDateTime
import java.lang.IllegalStateException

class NotRelevant (
    private val createdAt: LocalDateTime
): UserAddedContentStatus {
    override fun createdAt(): LocalDateTime = createdAt
    override fun statusType(): ContentStatusType = ContentStatusType.NOT_RELEVANT
    override fun remove(onDate: LocalDateTime, remover: User): UserAddedContentStatus = throw IllegalStateException("Cant remove irrelevant content")
    override fun activate(onDate: LocalDateTime, activator: User): UserAddedContentStatus = throw IllegalStateException("Cant activate irrelevant content")
    override fun markIrrelevant(onDate: LocalDateTime): UserAddedContentStatus = throw IllegalStateException("Already marked as irrelevant")
}