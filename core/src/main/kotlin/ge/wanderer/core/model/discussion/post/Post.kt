package ge.wanderer.core.model.discussion.post

import ge.wanderer.common.enums.UserContentType
import ge.wanderer.core.model.comment.IComment
import ge.wanderer.core.model.content.status.UserAddedContentStatus
import ge.wanderer.core.data.file.AttachedFile
import ge.wanderer.core.integration.user.User
import ge.wanderer.core.model.UpdateDiscussionElementData
import ge.wanderer.core.model.content.BaseUserContent
import ge.wanderer.core.model.report.Report
import ge.wanderer.core.model.report.ReportReason
import ge.wanderer.core.model.report.ReportReason.*
import ge.wanderer.core.model.discussion.DiscussionElement
import ge.wanderer.core.model.rating.IVote
import org.joda.time.LocalDateTime

class Post(
    id: Long,
    private val author: User,
    createdAt: LocalDateTime,
    private val content: String,
    private val routeCode: String,
    private val attachedFiles: List<AttachedFile>,
    status: UserAddedContentStatus,
    comments: MutableList<IComment> = mutableListOf(),
    votes: MutableList<IVote> = mutableListOf(),
    reports: MutableSet<Report> = mutableSetOf()
) : IPost, BaseUserContent(id, author, createdAt, status, comments, votes, reports) {

    override fun content(): String = content
    override fun attachedFiles(): List<AttachedFile> = attachedFiles
    override fun routeCode(): String = routeCode
    override fun contentType(): UserContentType = UserContentType.POST
    override fun acceptableReportReasons(): Set<ReportReason> = setOf(INAPPROPRIATE_CONTENT, OFFENSIVE_CONTENT)

    override fun update(updateData: UpdateDiscussionElementData): DiscussionElement =
        Post(
            id,
            author,
            createdAt,
            updateData.contentToUpdate,
            routeCode,
            updateData.files,
            status,
            comments,
            votes
        )

}