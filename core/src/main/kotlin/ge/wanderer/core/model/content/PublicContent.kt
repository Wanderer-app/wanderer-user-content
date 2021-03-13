package ge.wanderer.core.model.content

import ge.wanderer.core.model.content.status.PublicContentStatus
import ge.wanderer.core.model.user.User
import org.joda.time.LocalDate

interface PublicContent: Rateable,
    Commentable {
    fun creator(): User
    fun createdAt(): LocalDate
    fun status(): PublicContentStatus
    fun ban(onDate: LocalDate)
    fun unBan(onDate: LocalDate)
    fun delete(onDate: LocalDate)
    fun markIrrelevant(onDate: LocalDate)
}