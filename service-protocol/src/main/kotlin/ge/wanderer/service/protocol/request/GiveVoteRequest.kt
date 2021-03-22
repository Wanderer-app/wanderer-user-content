package ge.wanderer.service.protocol.request

import org.joda.time.LocalDateTime

data class GiveVoteRequest (
    val contentId: Long,
    val userId: Long,
    val date: LocalDateTime
)