package ge.wanderer.service.spring.model

import ge.wanderer.common.enums.UserContentType
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
    override fun content(): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun update(updateData: UpdateDiscussionElementData): DiscussionElement {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun attachedFiles(): List<AttachedFile> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun routeCode(): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun comments(): List<IComment> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun addComment(comment: IComment) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun id(): Long {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun creator(): User {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun createdAt(): LocalDateTime {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun isActive(): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun isRemoved(): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun statusUpdatedAt(): LocalDateTime {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun remove(onDate: LocalDateTime, remover: User) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun activate(onDate: LocalDateTime, activator: User) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun contentType(): UserContentType {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun giveVote(vote: IVote) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun rating(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun removeVotesBy(user: User, onDate: LocalDateTime) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun report(report: Report) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun reports(): Set<Report> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}