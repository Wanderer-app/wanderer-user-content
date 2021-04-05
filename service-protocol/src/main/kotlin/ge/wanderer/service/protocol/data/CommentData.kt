package ge.wanderer.service.protocol.data

import org.joda.time.LocalDateTime

data class CommentData (
    val id: Long,
    val author: UserData,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
    val text: String,
    val rating: Int,
    val isActive: Boolean,
    val isRemoved: Boolean,
    val responseNumber: Int,
    val responsesPreview: List<CommentData>
)