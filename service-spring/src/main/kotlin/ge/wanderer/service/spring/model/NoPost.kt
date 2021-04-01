package ge.wanderer.service.spring.model

import ge.wanderer.common.enums.UserContentType
import ge.wanderer.common.now
import ge.wanderer.core.data.file.AttachedFile
import ge.wanderer.core.integration.user.User
import ge.wanderer.core.model.UpdateDiscussionElementData
import ge.wanderer.core.model.comment.IComment
import ge.wanderer.core.model.discussion.DiscussionElement
import ge.wanderer.core.model.discussion.post.IPost
import ge.wanderer.core.model.rating.IVote
import ge.wanderer.core.model.report.Report
import org.joda.time.LocalDateTime

class NoPost: IPost {
    override fun content(): String = ""
    override fun update(updateData: UpdateDiscussionElementData): DiscussionElement = this
    override fun attachedFiles(): List<AttachedFile> = listOf()
    override fun routeCode(): String = ""
    override fun comments(): List<IComment> = listOf()
    override fun addComment(comment: IComment) { }
    override fun id(): Long = 0
    override fun creator(): User = User(0, "No", "User", 0, false)
    override fun createdAt(): LocalDateTime = now()
    override fun isActive(): Boolean = false
    override fun isRemoved(): Boolean = false
    override fun statusUpdatedAt(): LocalDateTime = now()
    override fun remove(onDate: LocalDateTime, remover: User) { }
    override fun activate(onDate: LocalDateTime, activator: User) { }
    override fun contentType(): UserContentType = UserContentType.POST
    override fun giveVote(vote: IVote) { }
    override fun rating(): Int = 0
    override fun removeVotesBy(user: User, onDate: LocalDateTime) { }
    override fun report(report: Report) { }
    override fun reports(): Set<Report> = setOf()
}