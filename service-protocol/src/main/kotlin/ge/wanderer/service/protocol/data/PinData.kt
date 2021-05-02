package ge.wanderer.service.protocol.data

import ge.wanderer.common.enums.PinType
import ge.wanderer.common.map.LatLng
import ge.wanderer.common.enums.VoteType
import org.joda.time.LocalDateTime

data class PinData (
    val id: Long,
    val creator: UserData,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
    val isActive: Boolean,
    val isRemoved: Boolean,
    val isRelevant: Boolean,
    val rating: RatingData,
    val commentsNumber: Int,
    val commentsPreview: List<CommentData>,
    val routeCode: String,
    val title: String,
    val text: String,
    val attachedFile: FileData?,
    val type: PinType,
    val location: LatLng,
    val userVoteDirection: VoteType?
)

data class PinMapData(
    val id: Long,
    val routeCode: String,
    val location: LatLng,
    val type: PinType,
    val createdAt: LocalDateTime,
    val title: String,
    val rating: Int
)