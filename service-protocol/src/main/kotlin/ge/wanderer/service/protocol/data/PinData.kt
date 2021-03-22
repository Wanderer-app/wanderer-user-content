package ge.wanderer.service.protocol.data

import ge.wanderer.core.integration.user.User
import ge.wanderer.core.model.content.status.ContentStatusType
import ge.wanderer.core.model.map.RouteElementContent
import org.joda.time.LocalDateTime

data class PinData (
    val id: Long,
    val creator: User,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
    val status: ContentStatusType,
    val rating: RatingData,
    val commentsPreview: List<CommentData>,
    val routeCode: String,
    val content: RouteElementContent
)