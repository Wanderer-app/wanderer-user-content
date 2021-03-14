package ge.wanderer.core.model.comment

import ge.wanderer.core.model.rating.Vote
import ge.wanderer.core.model.content.status.UserAddedContentStatus
import ge.wanderer.core.data.user.User
import org.joda.time.LocalDateTime

class Comment (
    private val id: Long,
    private val creator: User,
    private val createdAt: LocalDateTime,
    private val text: String,
    private val replies: MutableList<IComment>,
    private val votes: MutableList<Vote>,
    private var status: UserAddedContentStatus
): IComment {

    override fun text(): String = text

    override fun id(): Long = id
    override fun rating(): Int = votes.map { it.weight() }.sum()
    override fun comments(): MutableList<IComment> = replies
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
        replies.add(comment)
    }

}