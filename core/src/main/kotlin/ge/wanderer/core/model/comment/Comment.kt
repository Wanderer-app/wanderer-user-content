package ge.wanderer.core.model.comment

import ge.wanderer.common.enums.UserContentType
import ge.wanderer.core.model.content.status.UserAddedContentStatus
import ge.wanderer.core.integration.user.User
import ge.wanderer.core.model.UpdateCommentData
import ge.wanderer.core.model.content.BaseUserContent
import ge.wanderer.core.model.report.Report
import ge.wanderer.core.model.report.ReportReason
import ge.wanderer.core.model.report.ReportReason.*
import ge.wanderer.core.model.rating.IVote
import org.joda.time.LocalDateTime

class Comment(
    id: Long,
    creator: User,
    createdAt: LocalDateTime,
    private val text: String,
    status: UserAddedContentStatus,
    replies: MutableList<IComment> = mutableListOf(),
    votes: MutableList<IVote> = mutableListOf(),
    reports: MutableSet<Report> = mutableSetOf()
) : IComment, BaseUserContent(id, creator, createdAt, status, replies, votes, reports) {

    override fun text(): String = text
    override fun contentType(): UserContentType = UserContentType.COMMENT

    override fun acceptableReportReasons(): Set<ReportReason> =
        setOf(INAPPROPRIATE_CONTENT, OFFENSIVE_CONTENT)

    override fun update(updateData: UpdateCommentData): IComment =
        Comment(
            id,
            creator,
            createdAt,
            updateData.text,
            status,
            comments,
            votes
        )
}