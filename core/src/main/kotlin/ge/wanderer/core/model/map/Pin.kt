package ge.wanderer.core.model.map

import ge.wanderer.common.map.LatLng
import ge.wanderer.core.model.comment.IComment
import ge.wanderer.core.model.rating.Vote
import ge.wanderer.core.model.content.status.UserAddedContentStatus
import ge.wanderer.core.data.user.User
import ge.wanderer.core.model.content.BaseUserContent
import ge.wanderer.core.model.content.status.StatusType
import org.joda.time.LocalDateTime

class Pin(
    id: Long,
    creator: User,
    createdAt: LocalDateTime,
    private val location: LatLng,
    private val routeCode: String,
    private val type: MarkerType,
    private val content: RouteElementContent,
    status: UserAddedContentStatus,
    comments: MutableList<IComment>,
    votes: MutableList<Vote>
): IPin, BaseUserContent(id, creator, createdAt, status, comments, votes) {

    override fun location(): LatLng = location
    override fun routeCode(): String = routeCode
    override fun content(): RouteElementContent = content
    override fun type(): MarkerType = type
    override fun isRelevant(): Boolean = status.statusType() != StatusType.NOT_RELEVANT

    override fun markIrrelevant(onDate: LocalDateTime) {
        status = status.markIrrelevant(onDate)
    }



}