package ge.wanderer.core.model.discussion

import ge.wanderer.core.model.comment.Comment
import ge.wanderer.core.model.content.BasicPublicContent
import ge.wanderer.core.model.content.Vote
import ge.wanderer.core.model.content.status.PublicContentStatus
import ge.wanderer.core.model.file.AttachedFile
import ge.wanderer.core.model.user.User
import org.joda.time.LocalDate

class Post (
    author: User,
    createdAt: LocalDate,
    private val content: String,
    private val attachedFiles: List<AttachedFile>,
    status: PublicContentStatus,
    comments: MutableList<Comment>,
    votes: MutableList<Vote>
): DiscussionElement, BasicPublicContent(author, createdAt, status, comments, votes) {

    override fun content(): String = content
    override fun attachedFiles(): List<AttachedFile> = attachedFiles
}