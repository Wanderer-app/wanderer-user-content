package ge.wanderer.service.protocol.data

import ge.wanderer.common.enums.UserContentType
import ge.wanderer.core.data.file.AttachedFile
import ge.wanderer.core.integration.user.User
import org.joda.time.LocalDateTime

data class DiscussionElementData (
    val id: Long,
    val creator: User,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
    val isActive: Boolean,
    val isRemoved: Boolean,
    val ratingData: RatingData?,
    val commentsPreview: List<CommentData>,
    val commentsAmount: Int,
    val routeCode: String,
    val content: String,
    val attachedFiles: List<AttachedFile>,
    val type: UserContentType
)