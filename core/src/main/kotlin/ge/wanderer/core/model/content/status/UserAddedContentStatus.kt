package ge.wanderer.core.model.content.status

import org.joda.time.LocalDateTime

interface UserAddedContentStatus {
    fun createdAt(): LocalDateTime
    fun statusType(): StatusType
    fun remove(onDate: LocalDateTime): UserAddedContentStatus
    fun activate(onDate: LocalDateTime): UserAddedContentStatus
    fun markIrrelevant(onDate: LocalDateTime): UserAddedContentStatus
}