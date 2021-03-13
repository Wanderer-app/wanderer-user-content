package ge.wanderer.core.model.map

import ge.wanderer.common.map.LatLng
import ge.wanderer.core.model.content.PublicContent

interface RouteMarker: PublicContent {
    fun location(): LatLng
    fun routeCode(): String
    fun content(): MarkerContent
    fun type(): MarkerType
}