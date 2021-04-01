package ge.wanderer.service.protocol.data

import ge.wanderer.common.map.LatLng
import ge.wanderer.core.integration.user.User
import ge.wanderer.core.model.map.MarkerType
import ge.wanderer.core.model.map.RouteElementContent
import org.joda.time.LocalDateTime

data class PinData (
    val id: Long,
    val creator: User,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
    val isActive: Boolean,
    val isRemoved: Boolean,
    val isRelevant: Boolean,
    val rating: RatingData,
    val commentsNumber: Int,
    val commentsPreview: List<CommentData>,
    val routeCode: String,
    val content: RouteElementContent,
    val type: MarkerType
)

data class PinMapData(
    val id: Long,
    val routeCode: String,
    val location: LatLng,
    val type: MarkerType,
    val createdAt: LocalDateTime,
    val title: String,
    val rating: Int
)