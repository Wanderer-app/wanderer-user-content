package ge.wanderer.core.model.discussion.post

import ge.wanderer.core.model.comment.IComment
import ge.wanderer.core.model.content.Commentable
import ge.wanderer.core.model.content.Rateable
import ge.wanderer.core.model.rating.Vote
import ge.wanderer.core.model.content.status.UserAddedContentStatus
import ge.wanderer.core.data.file.AttachedFile
import ge.wanderer.core.data.user.User
import ge.wanderer.core.model.content.BaseUserContent
import ge.wanderer.core.model.discussion.DiscussionElement
import ge.wanderer.core.model.discussion.DiscussionElementType
import org.joda.time.LocalDateTime

class Post (
    id: Long,
    private val author: User,
    createdAt: LocalDateTime,
    private val content: String,
    private val routeCode: String,
    private val attachedFiles: List<AttachedFile>,
    status: UserAddedContentStatus,
    comments: MutableList<IComment>,
    votes: MutableList<Vote>
): DiscussionElement, Rateable, Commentable, BaseUserContent(id, author, createdAt, status, comments, votes) {

    override fun content(): String = content
    override fun attachedFiles(): List<AttachedFile> = attachedFiles
    override fun routeCode(): String = routeCode
    override fun type(): DiscussionElementType = DiscussionElementType.POST

}