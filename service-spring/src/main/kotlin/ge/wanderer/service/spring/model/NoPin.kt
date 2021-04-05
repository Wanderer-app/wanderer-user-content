package ge.wanderer.service.spring.model

import ge.wanderer.common.enums.PinType
import ge.wanderer.common.enums.ReportReason
import ge.wanderer.common.enums.UserContentType
import ge.wanderer.common.map.LatLng
import ge.wanderer.common.now
import ge.wanderer.core.integration.user.User
import ge.wanderer.core.model.comment.IComment
import ge.wanderer.core.model.map.IPin
import ge.wanderer.core.model.map.PinContent
import ge.wanderer.core.model.rating.IVote
import ge.wanderer.core.model.report.Report
import org.joda.time.LocalDateTime

class NoPin: IPin {

    override fun location(): LatLng = LatLng(0f, 0f)
    override fun routeCode(): String= ""
    override fun content(): PinContent = PinContent("", "", null)
    override fun type(): PinType = PinType.MISC_FACT
    override fun markIrrelevant(onDate: LocalDateTime) {}
    override fun isRelevant(): Boolean = false
    override fun update(newContent: PinContent) {}
    override fun giveVote(vote: IVote) {}
    override fun rating(): Int = 0
    override fun removeVotesBy(user: User, onDate: LocalDateTime) {}
    override fun id(): Long = 0
    override fun creator(): User = User(0, "No", "User", 0, false)
    override fun createdAt(): LocalDateTime = now()
    override fun isActive(): Boolean = false
    override fun isRemoved(): Boolean = false
    override fun statusUpdatedAt(): LocalDateTime = now()
    override fun remove(onDate: LocalDateTime, remover: User) {}
    override fun activate(onDate: LocalDateTime, activator: User) {}
    override fun contentType(): UserContentType = UserContentType.PIN
    override fun comments(): List<IComment> = listOf()
    override fun addComment(comment: IComment) {}
    override fun report(report: Report) {}
    override fun reports(): Set<Report> = setOf()
    override fun acceptableReportReasons(): Set<ReportReason> = setOf()
}