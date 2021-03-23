package ge.wanderer.service.protocol.data

import ge.wanderer.core.integration.user.User
import org.joda.time.LocalDateTime

data class CommentData (
    val id: Long,
    val author: User,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
    val text: String,
    val rating: Int,
    val responseNumber: Int,
    val responsesPreview: List<CommentData>
)