package ge.wanderer.core.model.report

import ge.wanderer.common.enums.ReportReason
import ge.wanderer.common.enums.UserContentType
import ge.wanderer.core.integration.user.User
import ge.wanderer.core.model.content.UserAddedContent
import org.joda.time.LocalDateTime

data class Report(
    val id: Long,
    val creator: User,
    val reportTime: LocalDateTime,
    val reason: ReportReason
) : UserAddedContent {
    override fun id(): Long = id

    override fun creator(): User = creator

    override fun createdAt(): LocalDateTime = reportTime

    override fun isActive(): Boolean = true

    override fun isRemoved(): Boolean = false

    override fun statusUpdatedAt(): LocalDateTime = reportTime

    override fun remove(onDate: LocalDateTime, remover: User) = error("Cant change status of report")

    override fun activate(onDate: LocalDateTime, activator: User) = error("Cant change status of report")

    override fun contentType(): UserContentType = UserContentType.REPORT

}