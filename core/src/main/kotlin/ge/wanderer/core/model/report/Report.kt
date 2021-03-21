package ge.wanderer.core.model.report

import ge.wanderer.core.integration.user.User
import org.joda.time.LocalDateTime


data class Report(
    val id: Long,
    val creator: User,
    val reportTime: LocalDateTime,
    val reason: ReportReason
)