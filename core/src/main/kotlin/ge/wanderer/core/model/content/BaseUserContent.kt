package ge.wanderer.core.model.content

import ge.wanderer.core.integration.user.User
import ge.wanderer.core.model.comment.IComment
import ge.wanderer.core.model.content.status.ContentStatusType
import ge.wanderer.core.model.content.status.UserAddedContentStatus
import ge.wanderer.core.model.rating.IVote
import ge.wanderer.core.model.report.Report
import org.joda.time.LocalDateTime

abstract class BaseUserContent(
    protected val id: Long,
    protected val creator: User,
    protected val createdAt: LocalDateTime,
    protected var status: UserAddedContentStatus,
    protected val comments: MutableList<IComment>,
    protected val votes: MutableList<IVote>,
    protected val reports: MutableSet<Report>
): CommentableContent, RateableContent, ReportableContent {
    
    override fun id(): Long = id
    override fun creator(): User = creator
    override fun createdAt(): LocalDateTime = createdAt
    override fun comments(): List<IComment> = comments.filter { it.isActive() }
    override fun isActive(): Boolean = status.statusType() == ContentStatusType.ACTIVE
    override fun isRemoved(): Boolean = status.statusType() == ContentStatusType.REMOVED
    override fun statusUpdatedAt(): LocalDateTime = status.createdAt()

    override fun remove(onDate: LocalDateTime, remover: User) {
        status = status.remove(onDate, remover)
    }

    override fun activate(onDate: LocalDateTime, activator: User) {
        status = status.activate(onDate, activator)
    }

    override fun addComment(comment: IComment) {
        comments.add(comment)
    }

    override fun giveVote(vote: IVote) {
        votes.add(vote)
    }

    override fun removeVotesBy(user: User, onDate: LocalDateTime) {
        votes
            .filter { it.isActive() }
            .filter { it.creator() == user }
            .forEach { it.remove(onDate, user) }
    }

    override fun rating(): Int =
        votes
            .filter { it.isActive() }
            .map { it.weight() }
            .sum()

    override fun report(report: Report) {
        check(acceptableReportReasons().contains(report.reason)) {
            "Cant report ${this.contentType()} with reason ${report.reason}"
        }
        check(reports.none { it.creator == report.creator }) { "You already reported this content" }
        reports.add(report)
    }

    override fun reports(): Set<Report> = reports.toSet()
}