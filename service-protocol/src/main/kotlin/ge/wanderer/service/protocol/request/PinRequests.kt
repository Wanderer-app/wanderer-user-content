package ge.wanderer.service.protocol.request

import ge.wanderer.common.map.LatLng
import ge.wanderer.core.model.map.MarkerType
import ge.wanderer.core.model.map.RouteElementContent
import org.joda.time.LocalDateTime

data class CreatePinRequest (
    val onDate: LocalDateTime,
    val userId: Long,
    val type: MarkerType,
    val content: RouteElementContent,
    val location: LatLng,
    val routeCode: String
)

data class UpdatePinRequest(
    val pinId: Long,
    val newContent: RouteElementContent,
    val updaterId: Long
)