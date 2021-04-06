package ge.wanderer.service.protocol.data

import ge.wanderer.common.enums.ReportReason
import ge.wanderer.core.integration.user.User
import org.joda.time.LocalDateTime

data class ReportData(
    val id: Long,
    val creator: User,
    val reportTime: LocalDateTime,
    val reason: ReportReason
)
