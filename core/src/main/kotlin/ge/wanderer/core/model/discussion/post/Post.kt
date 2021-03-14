package ge.wanderer.core.model.discussion.post

import ge.wanderer.core.model.comment.IComment
import ge.wanderer.core.model.content.Commentable
import ge.wanderer.core.model.content.Rateable
import ge.wanderer.core.model.rating.Vote
import ge.wanderer.core.model.content.status.UserAddedContentStatus
import ge.wanderer.core.data.file.AttachedFile
import ge.wanderer.core.data.user.User
import ge.wanderer.core.model.discussion.DiscussionElement
import ge.wanderer.core.model.discussion.DiscussionElementType
import org.joda.time.LocalDateTime

class Post (
    private val id: Long,
    private val author: User,
    private val createdAt: LocalDateTime,
    private val content: String,
    private val routeCode: String,
    private val attachedFiles: List<AttachedFile>,
    private var status: UserAddedContentStatus,
    private val comments: MutableList<IComment>,
    private val votes: MutableList<Vote>
): DiscussionElement, Rateable, Commentable {

    override fun content(): String = content
    override fun attachedFiles(): List<AttachedFile> = attachedFiles
    override fun routeCode(): String = routeCode
    override fun type(): DiscussionElementType = DiscussionElementType.POST

    override fun id(): Long = id
    override fun rating(): Int = votes.map { it.weight() }.sum()
    override fun comments(): MutableList<IComment> = comments
    override fun status(): UserAddedContentStatus = status
    override fun creator(): User = author
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