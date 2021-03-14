package ge.wanderer.core.model.map

import ge.wanderer.common.map.LatLng
import ge.wanderer.core.model.content.Commentable
import ge.wanderer.core.model.content.UserAddedContent
import ge.wanderer.core.model.content.Rateable
import org.joda.time.LocalDateTime

interface IPin: UserAddedContent, Rateable, Commentable {
    fun location(): LatLng
    fun routeCode(): String
    fun content(): RouteElementContent
    fun type(): MarkerType
    fun markIrrelevant(onDate: LocalDateTime)
}