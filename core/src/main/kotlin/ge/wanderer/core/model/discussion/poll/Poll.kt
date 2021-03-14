package ge.wanderer.core.model.discussion.poll

import ge.wanderer.common.toJson
import ge.wanderer.core.model.content.status.UserAddedContentStatus
import ge.wanderer.core.data.file.AttachedFile
import ge.wanderer.core.data.user.User
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
    private val answers: Set<PollAnswer>
): DiscussionElement {

    override fun content(): String = toJson(getContent())
    override fun attachedFiles(): List<AttachedFile> = listOf()
    override fun routeCode(): String = routeCode
    override fun type(): DiscussionElementType = DiscussionElementType.POLL

    override fun id(): Long = id
    override fun creator(): User = creator
    override fun createdAt(): LocalDateTime = createdAt
    override fun status(): UserAddedContentStatus = status

    override fun remove(onDate: LocalDateTime) {
        status = status.remove(onDate)
    }

    override fun activate(onDate: LocalDateTime) {
        status = status.activate(onDate)
    }

    private fun getContent(): PollContent {
        val totalAnswerers = answers.map { it.selectors().size }.sum()
        return PollContent(question, answers.map { it.data(totalAnswerers) }.toSet())
    }

}