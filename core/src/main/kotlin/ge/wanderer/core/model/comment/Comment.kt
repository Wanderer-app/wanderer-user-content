package ge.wanderer.core.model.comment

import ge.wanderer.common.enums.UserContentType
import ge.wanderer.core.model.content.status.UserAddedContentStatus
import ge.wanderer.core.integration.user.User
import ge.wanderer.core.model.UpdateCommentData
import ge.wanderer.core.model.content.BaseUserContent
import ge.wanderer.core.model.rating.IVote
import org.joda.time.LocalDateTime

class Comment(
    id: Long,
    creator: User,
    createdAt: LocalDateTime,
    private val text: String,
    replies: MutableList<IComment>,
    votes: MutableList<IVote>,
    status: UserAddedContentStatus
) : IComment, BaseUserContent(id, creator, createdAt, status, replies, votes) {

    override fun text(): String = text
    override fun contentType(): UserContentType = UserContentType.COMMENT

    override fun update(updateData: UpdateCommentData): IComment =
        Comment(
            id,
            creator,
            createdAt,
            updateData.text,
            comments,
            votes,
            status
        )
}