package ge.wanderer.service.protocol.data

import ge.wanderer.common.enums.UserContentType
import ge.wanderer.core.integration.user.User
import ge.wanderer.core.model.content.status.ContentStatusType
import org.joda.time.LocalDateTime

data class DiscussionElementData (
    val id: Long,
    val creator: User,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
    val status: ContentStatusType,
    val rating: RatingData,
    val commentsPreview: List<CommentData>,
    val commentsAmount: Int,
    val routeCode: String,
    val content: String,
    val type: UserContentType
)