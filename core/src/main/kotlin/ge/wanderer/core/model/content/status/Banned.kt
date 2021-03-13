package ge.wanderer.core.model.content.status

import org.joda.time.LocalDate
import java.lang.IllegalStateException

class Banned (
    private val createdAt: LocalDate
): PublicContentStatus {
    override fun createdAt(): LocalDate = createdAt
    override fun statusType(): StatusType =
        StatusType.BANNED
    override fun ban(onDate: LocalDate): PublicContentStatus = throw IllegalStateException("Content already banned")
    override fun delete(onDate: LocalDate): PublicContentStatus =
        Deleted(onDate)
    override fun unBan(onDate: LocalDate): PublicContentStatus =
        Active(onDate)
    override fun markIrrelevant(onDate: LocalDate): PublicContentStatus = throw IllegalStateException("Already banned content can't be marked as irrelevant")

}