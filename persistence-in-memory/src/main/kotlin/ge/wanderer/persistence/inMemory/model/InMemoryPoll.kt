package ge.wanderer.persistence.inMemory.model

import ge.wanderer.common.enums.UserContentType
import ge.wanderer.core.integration.file.AttachedFile
import ge.wanderer.core.integration.user.User
import ge.wanderer.core.model.UpdateDiscussionElementData
import ge.wanderer.core.model.comment.IComment
import ge.wanderer.core.model.discussion.poll.IPoll
import ge.wanderer.core.model.discussion.poll.IPollAnswer
import ge.wanderer.core.model.discussion.poll.PollAnswerData
import ge.wanderer.persistence.inMemory.repository.PollRepositoryImpl
import ge.wanderer.persistence.repository.CommentRepository
import org.joda.time.LocalDateTime

class InMemoryPoll(
    private val id: Long,
    private val poll: IPoll,
    private val pollRepository: PollRepositoryImpl,
    private val commentRepository: CommentRepository
): IPoll {

    override fun addAnswer(answer: IPollAnswer) {
        poll.addAnswer(pollRepository.makeAnswerPersistent(answer))
    }

    override fun selectAnswer(answerId: Long, user: User) {
        poll.selectAnswer(answerId, user)
    }

    override fun answersData(): Set<PollAnswerData> = poll.answersData()

    override fun answers(): List<IPollAnswer> = poll.answers()

    override fun content(): String = poll.content()

    override fun update(updateData: UpdateDiscussionElementData) {
        poll.update(updateData)
    }

    override fun attachedFiles(): List<AttachedFile> = poll.attachedFiles()

    override fun routeCode(): String = poll.routeCode()

    override fun comments(): List<IComment> = poll.comments()

    override fun addComment(comment: IComment) {
        poll.addComment(commentRepository.persist(comment))
    }

    override fun id(): Long = id

    override fun creator(): User = poll.creator()

    override fun createdAt(): LocalDateTime = poll.createdAt()

    override fun isActive(): Boolean = poll.isActive()

    override fun isRemoved(): Boolean = poll.isRemoved()

    override fun statusUpdatedAt(): LocalDateTime = poll.statusUpdatedAt()

    override fun remove(onDate: LocalDateTime, remover: User) {
        poll.remove(onDate, remover)
    }

    override fun activate(onDate: LocalDateTime, activator: User) {
        poll.activate(onDate, activator)
    }

    override fun contentType(): UserContentType = poll.contentType()
}