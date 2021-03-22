package ge.wanderer.service.protocol.request

import ge.wanderer.common.map.LatLng
import ge.wanderer.core.integration.user.User
import ge.wanderer.core.model.map.MarkerType
import ge.wanderer.core.model.map.RouteElementContent
import org.joda.time.LocalDateTime

data class CreatePinRequest (
    val onDate: LocalDateTime,
    val user: User,
    val type: MarkerType,
    val content: RouteElementContent,
    val location: LatLng,
    val routeCode: String
)