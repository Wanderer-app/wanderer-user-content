package ge.wanderer.core.model.map

import ge.wanderer.common.map.LatLng
import ge.wanderer.core.model.comment.Comment
import ge.wanderer.core.model.content.BasicPublicContent
import ge.wanderer.core.model.content.Vote
import ge.wanderer.core.model.content.status.PublicContentStatus
import ge.wanderer.core.model.user.User
import org.joda.time.LocalDate

class Pin (
    creator: User,
    createdAt: LocalDate,
    private val location: LatLng,
    private val routeCode: String,
    private val type: MarkerType,
    private val content: MarkerContent,
    comments: MutableList<Comment>,
    votes: MutableList<Vote>,
    status: PublicContentStatus
): RouteMarker, BasicPublicContent(creator, createdAt, status, comments, votes) {

    override fun location(): LatLng = location
    override fun routeCode(): String = routeCode
    override fun content(): MarkerContent = content
    override fun type(): MarkerType = type

}