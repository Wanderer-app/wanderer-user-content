package ge.wanderer.persistence.inMemory.model

import ge.wanderer.common.enums.ReportReason
import ge.wanderer.common.enums.UserContentType
import ge.wanderer.core.data.file.AttachedFile
import ge.wanderer.core.integration.user.User
import ge.wanderer.core.model.UpdateDiscussionElementData
import ge.wanderer.core.model.comment.IComment
import ge.wanderer.core.model.discussion.post.IPost
import ge.wanderer.core.model.rating.IVote
import ge.wanderer.core.model.report.Report
import ge.wanderer.persistence.repository.CommentRepository
import org.joda.time.LocalDateTime

class InMemoryPost(
    private val id: Long,
    private val post: IPost,
    private val commentRepository: CommentRepository
): IPost {

    override fun content(): String = post.content()

    override fun update(updateData: UpdateDiscussionElementData) {
        post.update(updateData)
    }

    override fun attachedFiles(): List<AttachedFile> = post.attachedFiles()

    override fun routeCode(): String = post.routeCode()

    override fun comments(): List<IComment> = post.comments()

    override fun addComment(comment: IComment) {
        post.addComment(commentRepository.persist(comment))
    }

    override fun id(): Long = id

    override fun creator(): User = post.creator()

    override fun createdAt(): LocalDateTime = post.createdAt()

    override fun isActive(): Boolean = post.isActive()

    override fun isRemoved(): Boolean = post.isRemoved()

    override fun statusUpdatedAt(): LocalDateTime = post.statusUpdatedAt()

    override fun remove(onDate: LocalDateTime, remover: User) {
        post.remove(onDate, remover)
    }

    override fun activate(onDate: LocalDateTime, activator: User) {
        post.activate(onDate, activator)
    }

    override fun contentType(): UserContentType = post.contentType()

    override fun giveVote(vote: IVote) {
        post.giveVote(vote)
    }

    override fun rating(): Int = post.rating()

    override fun removeVotesBy(user: User, onDate: LocalDateTime) {
        post.removeVotesBy(user, onDate)
    }

    override fun report(report: Report) {
        post.report(report)
    }

    override fun reports(): Set<Report> = post.reports()

    override fun acceptableReportReasons(): Set<ReportReason> = post.acceptableReportReasons()

}