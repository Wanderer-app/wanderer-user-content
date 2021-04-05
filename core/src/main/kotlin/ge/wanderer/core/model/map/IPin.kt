package ge.wanderer.core.model.map

import ge.wanderer.common.enums.PinType
import ge.wanderer.common.map.LatLng
import ge.wanderer.core.model.content.CommentableContent
import ge.wanderer.core.model.content.RateableContent
import ge.wanderer.core.model.content.ReportableContent
import org.joda.time.LocalDateTime

interface IPin: RateableContent, CommentableContent, ReportableContent {
    fun location(): LatLng
    fun routeCode(): String
    fun content(): PinContent
    fun type(): PinType
    fun markIrrelevant(onDate: LocalDateTime)
    fun isRelevant(): Boolean
    fun update(newContent: PinContent)
}