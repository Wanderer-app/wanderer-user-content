package ge.wanderer.core.model.discussion.poll

import ge.wanderer.common.enums.UserContentType
import ge.wanderer.common.functions.toJson
import ge.wanderer.core.integration.file.AttachedFile
import ge.wanderer.core.integration.user.User
import ge.wanderer.core.model.UpdateDiscussionElementData
import ge.wanderer.core.model.comment.IComment
import ge.wanderer.core.model.content.status.ContentStatusType
import ge.wanderer.core.model.content.status.UserAddedContentStatus
import org.joda.time.LocalDateTime

class Poll(
    private val id: Long,
    private val creator: User,
    private val createdAt: LocalDateTime,
    private var status: UserAddedContentStatus,
    private val routeCode: String,
    private var question: String,
    private val answers: MutableSet<IPollAnswer>,
    private val comments: MutableList<IComment>
): IPoll {
    override fun content(): String = toJson(PollContent(question, answersData()))
    override fun attachedFiles(): List<AttachedFile> = listOf()
    override fun routeCode(): String = routeCode

    override fun id(): Long = id
    override fun creator(): User = creator
    override fun createdAt(): LocalDateTime = createdAt
    override fun isActive(): Boolean = status.statusType() == ContentStatusType.ACTIVE
    override fun isRemoved(): Boolean = status.statusType() == ContentStatusType.REMOVED
    override fun statusUpdatedAt(): LocalDateTime = status.createdAt()
    override fun contentType(): UserContentType = UserContentType.POLL

    private fun activeAnswers() = answers.filter { it.isActive() }

    override fun addAnswer(answer: IPollAnswer) {
        check(activeAnswers().none { it.text() == answer.text() }) { "Such answer already exists" }
        answers.add(answer)
    }

    override fun selectAnswer(answerId: Long, user: User) {
        answers
            .filter { it.id() != answerId }
            .filter { it.selectors().contains(user) }
            .forEach { it.selectBy(user) }
        answers
            .asSequence()
            .filter { it.isActive() }
            .first { it.id() == answerId }
            .selectBy(user)
    }

    override fun answersData(): Set<PollAnswerData> {
        val activeAnswers = activeAnswers()

        val totalAnswerers = activeAnswers.map { it.selectors().size }.sum()
        return activeAnswers.map { it.data(totalAnswerers) }.toSet()
    }

    override fun answers(): List<IPollAnswer> = activeAnswers()

    override fun comments(): List<IComment> = comments.filter { it.isActive() }
    override fun addComment(comment: IComment): IComment {
        comments.add(comment)
        return comment
    }

    override fun remove(onDate: LocalDateTime, remover: User) {
        status = status.remove(onDate, remover)
    }
    override fun activate(onDate: LocalDateTime, activator: User) {
        status = status.activate(onDate, activator)
    }

    override fun update(updateData: UpdateDiscussionElementData) {
        question = updateData.contentToUpdate
    }

}