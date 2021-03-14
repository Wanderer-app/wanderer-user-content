package ge.wanderer.core.model.content

import ge.wanderer.core.model.content.status.UserAddedContentStatus
import ge.wanderer.core.data.user.User
import org.joda.time.LocalDateTime

interface UserAddedContent {
    fun id(): Long
    fun creator(): User
    fun createdAt(): LocalDateTime
    fun status(): UserAddedContentStatus
    fun remove(onDate: LocalDateTime)
    fun activate(onDate: LocalDateTime)
}