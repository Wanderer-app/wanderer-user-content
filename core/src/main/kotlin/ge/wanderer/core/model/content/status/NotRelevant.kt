package ge.wanderer.core.model.content.status

import org.joda.time.LocalDate
import java.lang.IllegalStateException

class NotRelevant (
    private val createdAt: LocalDate
): PublicContentStatus {
    override fun createdAt(): LocalDate = createdAt
    override fun statusType(): StatusType =
        StatusType.NOT_RELEVANT
    override fun ban(onDate: LocalDate): PublicContentStatus = throw IllegalStateException("Cant ban irrelevant content")
    override fun delete(onDate: LocalDate): PublicContentStatus =
        Deleted(onDate)
    override fun unBan(onDate: LocalDate): PublicContentStatus = throw IllegalStateException("Cant unban irrelevant content")
    override fun markIrrelevant(onDate: LocalDate): PublicContentStatus = throw IllegalStateException("Already marked as irrelevant")
}