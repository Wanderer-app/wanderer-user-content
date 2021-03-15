package ge.wanderer.core.model.discussion.poll

import ge.wanderer.common.toJson
import ge.wanderer.core.model.content.status.UserAddedContentStatus
import ge.wanderer.core.data.file.AttachedFile
import ge.wanderer.core.data.user.User
import ge.wanderer.core.model.comment.IComment
import ge.wanderer.core.model.content.status.StatusType
import ge.wanderer.core.model.discussion.DiscussionElement
import ge.wanderer.core.model.discussion.DiscussionElementType
import org.joda.time.LocalDateTime
import java.math.BigDecimal

class Poll(
    private val id: Long,
    private val creator: User,
    private val createdAt: LocalDateTime,
    private var status: UserAddedContentStatus,
    private val routeCode: String,
    private val question: String,
    private val answers: MutableSet<IPollAnswer>,
    private val comments: MutableList<IComment>
): IPoll {
    override fun content(): String = toJson(PollContent(question, answersData()))
    override fun attachedFiles(): List<AttachedFile> = listOf()
    override fun routeCode(): String = routeCode
    override fun type(): DiscussionElementType = DiscussionElementType.POLL

    override fun id(): Long = id
    override fun creator(): User = creator
    override fun createdAt(): LocalDateTime = createdAt
    override fun isActive(): Boolean = status.statusType() == StatusType.ACTIVE
    override fun isRemoved(): Boolean = status.statusType() == StatusType.REMOVED
    override fun statusUpdatedAt(): LocalDateTime = status.createdAt()

    override fun addAnswer(answer: IPollAnswer) {
        answers.add(answer)
    }

    override fun answersData(): Set<PollAnswerData> {
        val totalAnswerers = answers.map { it.selectors().size }.sum()
        return answers
            .filter { it.isActive() }
            .map { it.data(totalAnswerers) }.toSet()
    }

    override fun remove(onDate: LocalDateTime) {
        status = status.remove(onDate)
    }

    override fun activate(onDate: LocalDateTime) {
        status = status.activate(onDate)
    }

    override fun comments(): List<IComment> = comments.filter { it.isActive() }
    override fun addComment(comment: IComment) { comments.add(comment) }

}