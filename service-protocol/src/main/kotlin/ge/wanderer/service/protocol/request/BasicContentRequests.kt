package ge.wanderer.service.protocol.request

import ge.wanderer.core.model.report.ReportReason
import org.joda.time.LocalDateTime

data class OperateOnContentRequest (
    val contentId: Long,
    val userId: Long,
    val date: LocalDateTime
)

data class ReportContentRequest (
    val contentId: Long,
    val userId: Long,
    val date: LocalDateTime,
    val reportReason: ReportReason
)