package ge.wanderer.core.model.content

import ge.wanderer.core.data.user.User
import ge.wanderer.core.model.comment.IComment
import ge.wanderer.core.model.content.status.StatusType
import ge.wanderer.core.model.content.status.UserAddedContentStatus
import ge.wanderer.core.model.rating.Vote
import org.joda.time.LocalDateTime

abstract class BaseUserContent(
    protected val id: Long,
    protected val creator: User,
    protected val createdAt: LocalDateTime,
    protected var status: UserAddedContentStatus,
    protected val comments: MutableList<IComment>,
    protected val votes: MutableList<Vote>
): UserAddedContent, Commentable, Rateable {
    
    override fun id(): Long = id
    override fun creator(): User = creator
    override fun createdAt(): LocalDateTime = createdAt
    override fun comments(): List<IComment> = comments.filter { it.isActive() }
    override fun isActive(): Boolean = status.statusType() == StatusType.ACTIVE
    override fun isRemoved(): Boolean = status.statusType() == StatusType.REMOVED
    override fun statusUpdatedAt(): LocalDateTime = status.createdAt()


    override fun remove(onDate: LocalDateTime) {
        status = status.remove(onDate)
    }

    override fun activate(onDate: LocalDateTime) {
        status = status.activate(onDate)
    }

    override fun addComment(comment: IComment) {
        comments.add(comment)
    }

    override fun giveVote(vote: Vote) {
        votes.add(vote)
    }

    override fun rating(): Int =
        votes
            .filter { it.isActive() }
            .map { it.weight() }
            .sum()

}