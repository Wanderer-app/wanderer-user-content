package ge.wanderer.core.model.discussion.post

import ge.wanderer.common.enums.ReportReason
import ge.wanderer.common.enums.ReportReason.INAPPROPRIATE_CONTENT
import ge.wanderer.common.enums.ReportReason.OFFENSIVE_CONTENT
import ge.wanderer.common.enums.UserContentType
import ge.wanderer.core.integration.file.AttachedFile
import ge.wanderer.core.integration.user.User
import ge.wanderer.core.model.UpdateDiscussionElementData
import ge.wanderer.core.model.comment.IComment
import ge.wanderer.core.model.content.BaseUserContent
import ge.wanderer.core.model.content.status.UserAddedContentStatus
import ge.wanderer.core.model.rating.IVote
import ge.wanderer.core.model.report.Report
import org.joda.time.LocalDateTime

class Post(
    id: Long,
    private val author: User,
    createdAt: LocalDateTime,
    private var content: String,
    private val routeCode: String,
    private var attachedFiles: List<AttachedFile>,
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

    override fun update(updateData: UpdateDiscussionElementData) {
        content = updateData.contentToUpdate
        attachedFiles = updateData.files
    }

}