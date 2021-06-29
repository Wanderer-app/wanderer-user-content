package ge.wanderer.service.protocol.request

import org.joda.time.LocalDateTime

data class UpdateCommentRequest(
    val commentId: Long,
    val updaterId: String,
    val text: String
)

data class AddCommentRequest(
    val contentId: Long,
    val commenterId: String,
    val commentContent: String,
    val date: LocalDateTime
)