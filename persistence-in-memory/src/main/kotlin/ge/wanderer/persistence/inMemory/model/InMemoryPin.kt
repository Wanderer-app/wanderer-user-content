package ge.wanderer.persistence.inMemory.model

import ge.wanderer.common.enums.PinType
import ge.wanderer.common.enums.ReportReason
import ge.wanderer.common.enums.UserContentType
import ge.wanderer.common.map.LatLng
import ge.wanderer.core.integration.user.User
import ge.wanderer.core.model.comment.IComment
import ge.wanderer.core.model.map.IPin
import ge.wanderer.core.model.map.PinContent
import ge.wanderer.core.model.rating.IVote
import ge.wanderer.core.model.report.Report
import ge.wanderer.persistence.repository.CommentRepository
import org.joda.time.LocalDateTime

class InMemoryPin(
    private val id: Long,
    private val pin: IPin,
    private val commentRepository: CommentRepository
): IPin {

    override fun comments(): List<IComment> = pin.comments()

    override fun addComment(comment: IComment) {
        pin.addComment(commentRepository.persist(comment))
    }

    override fun report(report: Report) {
        pin.report(report)
    }

    override fun reports(): Set<Report> = pin.reports()

    override fun acceptableReportReasons(): Set<ReportReason> = pin.acceptableReportReasons()
    override fun location(): LatLng = pin.location()

    override fun routeCode(): String = pin.routeCode()

    override fun content(): PinContent = pin.content()

    override fun type(): PinType = pin.type()

    override fun markIrrelevant(onDate: LocalDateTime) {
        pin.markIrrelevant(onDate)
    }

    override fun isRelevant(): Boolean = pin.isRelevant()


    override fun update(newContent: PinContent) {
        pin.update(newContent)
    }

    override fun giveVote(vote: IVote) {
        pin.giveVote(vote)
    }

    override fun rating(): Int = pin.rating()

    override fun removeVotesBy(user: User, onDate: LocalDateTime) {
        pin.removeVotesBy(user, onDate)
    }

    override fun id(): Long = id

    override fun creator(): User = pin.creator()

    override fun createdAt(): LocalDateTime = pin.createdAt()

    override fun isActive(): Boolean = pin.isActive()

    override fun isRemoved(): Boolean = pin.isRemoved()

    override fun statusUpdatedAt(): LocalDateTime = pin.statusUpdatedAt()

    override fun remove(onDate: LocalDateTime, remover: User) {
        pin.remove(onDate, remover)
    }

    override fun activate(onDate: LocalDateTime, activator: User) {
        pin.activate(onDate, activator)
    }

    override fun contentType(): UserContentType = pin.contentType()
}