package ge.wanderer.service.protocol.request

import org.joda.time.LocalDateTime

data class ReportContentRequest (
    val userId: Long,
    val contentId: Long,
    val date: LocalDateTime
)