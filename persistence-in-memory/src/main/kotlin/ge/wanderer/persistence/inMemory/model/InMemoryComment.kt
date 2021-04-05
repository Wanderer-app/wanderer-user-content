package ge.wanderer.persistence.inMemory.model

import ge.wanderer.common.enums.ReportReason
import ge.wanderer.common.enums.UserContentType
import ge.wanderer.core.integration.user.User
import ge.wanderer.core.model.UpdateCommentData
import ge.wanderer.core.model.comment.IComment
import ge.wanderer.core.model.rating.IVote
import ge.wanderer.core.model.report.Report
import ge.wanderer.persistence.repository.CommentRepository

import org.joda.time.LocalDateTime

class InMemoryComment(
    private val id: Long,
    private val comment: IComment,
    private val commentRepository: CommentRepository
): IComment {

    override fun comments(): List<IComment> = comment.comments()

    override fun addComment(comment: IComment) {
        this.comment.addComment(commentRepository.persist(comment))
    }

    override fun report(report: Report) {
        comment.report(report)
    }

    override fun reports(): Set<Report> = comment.reports()

    override fun acceptableReportReasons(): Set<ReportReason> = comment.acceptableReportReasons()

    override fun text(): String = comment.text()

    override fun update(updateData: UpdateCommentData) {
        comment.update(updateData)
    }

    override fun giveVote(vote: IVote) {
        comment.giveVote(vote)
    }

    override fun rating(): Int = comment.rating()

    override fun removeVotesBy(user: User, onDate: LocalDateTime) {
        comment.removeVotesBy(user, onDate)
    }

    override fun id(): Long = id

    override fun creator(): User = comment.creator()

    override fun createdAt(): LocalDateTime = comment.createdAt()

    override fun isActive(): Boolean = comment.isActive()

    override fun isRemoved(): Boolean = comment.isRemoved()

    override fun statusUpdatedAt(): LocalDateTime = comment.statusUpdatedAt()

    override fun remove(onDate: LocalDateTime, remover: User) {
        comment.remove(onDate, remover)
    }

    override fun activate(onDate: LocalDateTime, activator: User) {
        comment.activate(onDate, activator)
    }

    override fun contentType(): UserContentType = comment.contentType()
}