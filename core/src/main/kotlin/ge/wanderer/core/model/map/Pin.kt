package ge.wanderer.core.model.map

import ge.wanderer.common.enums.PinType
import ge.wanderer.common.enums.ReportReason
import ge.wanderer.common.enums.ReportReason.*
import ge.wanderer.common.enums.UserContentType
import ge.wanderer.common.map.LatLng
import ge.wanderer.core.integration.user.User
import ge.wanderer.core.model.comment.IComment
import ge.wanderer.core.model.content.BaseUserContent
import ge.wanderer.core.model.content.status.ContentStatusType
import ge.wanderer.core.model.content.status.UserAddedContentStatus
import ge.wanderer.core.model.rating.IVote
import ge.wanderer.core.model.report.Report
import org.joda.time.LocalDateTime

class Pin(
    id: Long,
    creator: User,
    createdAt: LocalDateTime,
    private val location: LatLng,
    private val routeCode: String,
    private val type: PinType,
    private var content: PinContent,
    status: UserAddedContentStatus,
    comments: MutableList<IComment> = mutableListOf(),
    votes: MutableList<IVote> = mutableListOf(),
    reports: MutableSet<Report> = mutableSetOf()
) : IPin, BaseUserContent(id, creator, createdAt, status, comments, votes, reports) {

    override fun location(): LatLng = location
    override fun routeCode(): String = routeCode
    override fun content(): PinContent = content
    override fun type(): PinType = type
    override fun isRelevant(): Boolean = status.statusType() != ContentStatusType.NOT_RELEVANT
    override fun contentType(): UserContentType = UserContentType.PIN

    override fun markIrrelevant(onDate: LocalDateTime) {
        status = status.markIrrelevant(onDate)
    }

    override fun acceptableReportReasons(): Set<ReportReason> =
        setOf(INAPPROPRIATE_CONTENT, IRRELEVANT, OFFENSIVE_CONTENT)

    override fun update(newContent: PinContent) {
        content = newContent
    }

}