package ge.wanderer.core.model.content.status

import ge.wanderer.core.integration.user.User
import org.joda.time.LocalDateTime

interface UserAddedContentStatus {
    fun createdAt(): LocalDateTime
    fun statusType(): ContentStatusType
    fun remove(onDate: LocalDateTime, remover: User): UserAddedContentStatus
    fun activate(onDate: LocalDateTime, activator: User): UserAddedContentStatus
    fun markIrrelevant(onDate: LocalDateTime): UserAddedContentStatus
}