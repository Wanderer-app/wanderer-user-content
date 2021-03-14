package ge.wanderer.core.model.map

import ge.wanderer.common.map.LatLng
import ge.wanderer.core.model.comment.IComment
import ge.wanderer.core.model.rating.Vote
import ge.wanderer.core.model.content.status.UserAddedContentStatus
import ge.wanderer.core.data.user.User
import org.joda.time.LocalDateTime

class Pin(
    private val id: Long,
    private val creator: User,
    private val createdAt: LocalDateTime,
    private val location: LatLng,
    private val routeCode: String,
    private val type: MarkerType,
    private val content: RouteElementContent,
    private var status: UserAddedContentStatus,
    private val comments: MutableList<IComment>,
    private val votes: MutableList<Vote>
): IPin {

    override fun location(): LatLng = location
    override fun routeCode(): String = routeCode
    override fun content(): RouteElementContent = content
    override fun type(): MarkerType = type

    override fun markIrrelevant(onDate: LocalDateTime) {
        status = status.markIrrelevant(onDate)
    }

    override fun id(): Long = id
    override fun rating(): Int = votes.map { it.weight() }.sum()
    override fun comments(): MutableList<IComment> = comments
    override fun status(): UserAddedContentStatus = status
    override fun creator(): User = creator
    override fun createdAt(): LocalDateTime = createdAt

    override fun remove(onDate: LocalDateTime) {
        status = status.remove(onDate)
    }

    override fun activate(onDate: LocalDateTime) {
        status = status.activate(onDate)
    }

    override fun giveVote(vote: Vote) {
        votes.add(vote)
    }

    override fun addComment(comment: IComment) {
        comments.add(comment)
    }

}