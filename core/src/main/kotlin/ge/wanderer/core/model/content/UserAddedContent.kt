package ge.wanderer.core.model.content

import ge.wanderer.common.enums.UserContentType
import ge.wanderer.core.integration.user.User
import org.joda.time.LocalDateTime

interface UserAddedContent {
    fun id(): Long
    fun creator(): User
    fun createdAt(): LocalDateTime
    fun isActive(): Boolean
    fun isRemoved(): Boolean
    fun statusUpdatedAt(): LocalDateTime
    fun remove(onDate: LocalDateTime, remover: User)
    fun activate(onDate: LocalDateTime, activator: User)
    fun contentType(): UserContentType
}