package ge.wanderer.core.model.content.status

import org.joda.time.LocalDate
import java.lang.IllegalStateException

class Active (
    private val createdAt: LocalDate
): PublicContentStatus {
    override fun createdAt(): LocalDate = createdAt
    override fun statusType(): StatusType =
        StatusType.ACTIVE
    override fun ban(onDate: LocalDate): PublicContentStatus =
        Banned(onDate)
    override fun delete(onDate: LocalDate): PublicContentStatus =
        Deleted(onDate)
    override fun unBan(onDate: LocalDate): PublicContentStatus = throw IllegalStateException("Content already active")
    override fun markIrrelevant(onDate: LocalDate): PublicContentStatus =
        NotRelevant(onDate)
}