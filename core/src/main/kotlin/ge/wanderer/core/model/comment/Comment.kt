package ge.wanderer.core.model.comment

import ge.wanderer.core.model.content.status.UserAddedContentStatus
import ge.wanderer.core.data.user.User
import ge.wanderer.core.model.content.BaseUserContent
import ge.wanderer.core.model.rating.IVote
import org.joda.time.LocalDateTime

class Comment (
    id: Long,
    creator: User,
    createdAt: LocalDateTime,
    private val text: String,
    replies: MutableList<IComment>,
    votes: MutableList<IVote>,
    status: UserAddedContentStatus
): IComment, BaseUserContent(id, creator, createdAt, status, replies, votes) {

    override fun text(): String = text

}