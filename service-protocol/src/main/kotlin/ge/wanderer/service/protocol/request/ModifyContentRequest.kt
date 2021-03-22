package ge.wanderer.service.protocol.request

import org.joda.time.LocalDateTime

data class ModifyContentRequest (
    val contentId: Long,
    val date: LocalDateTime,
    val userId: Long
)