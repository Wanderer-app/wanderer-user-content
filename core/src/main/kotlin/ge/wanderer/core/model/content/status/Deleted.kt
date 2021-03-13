package ge.wanderer.core.model.content.status

import org.joda.time.LocalDate
import java.lang.IllegalStateException

class Deleted (
    private val createdAt: LocalDate
): PublicContentStatus {
    override fun createdAt(): LocalDate = createdAt
    override fun statusType(): StatusType =
        StatusType.DELETED
    override fun ban(onDate: LocalDate): PublicContentStatus = throw IllegalStateException("Cant ban deleted content")
    override fun delete(onDate: LocalDate): PublicContentStatus = throw IllegalStateException("Content already deleted")
    override fun unBan(onDate: LocalDate): PublicContentStatus = throw IllegalStateException("Cant unban deleted content")
    override fun markIrrelevant(onDate: LocalDate): PublicContentStatus = throw IllegalStateException("Content already deleted")
}