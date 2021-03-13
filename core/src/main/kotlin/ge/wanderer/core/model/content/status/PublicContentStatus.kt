package ge.wanderer.core.model.content.status

import org.joda.time.LocalDate

interface PublicContentStatus {
    fun createdAt(): LocalDate
    fun statusType(): StatusType

    fun ban(onDate: LocalDate): PublicContentStatus
    fun delete(onDate: LocalDate): PublicContentStatus
    fun unBan(onDate: LocalDate): PublicContentStatus
    fun markIrrelevant(onDate: LocalDate): PublicContentStatus
}