package ge.wanderer.core.model.map

import ge.wanderer.common.map.LatLng
import ge.wanderer.core.model.content.CommentableContent
import ge.wanderer.core.model.content.UserAddedContent
import ge.wanderer.core.model.content.RateableContent
import ge.wanderer.core.model.content.ReportableContent
import org.joda.time.LocalDateTime

interface IPin: RateableContent, CommentableContent, ReportableContent {
    fun location(): LatLng
    fun routeCode(): String
    fun content(): RouteElementContent
    fun type(): MarkerType
    fun markIrrelevant(onDate: LocalDateTime)
    fun isRelevant(): Boolean
    fun update(content: RouteElementContent): IPin
}