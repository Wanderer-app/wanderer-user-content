package ge.wanderer.core.model.comment

import ge.wanderer.core.model.content.BasicPublicContent
import ge.wanderer.core.model.content.Vote
import ge.wanderer.core.model.content.status.PublicContentStatus
import ge.wanderer.core.model.user.User
import org.joda.time.LocalDate

class BasicComment (
    creator: User,
    createdAt: LocalDate,
    private val text: String,
    replies: MutableList<Comment>,
    votes: MutableList<Vote>,
    status: PublicContentStatus
): Comment, BasicPublicContent(creator, createdAt, status, replies, votes) {

    override fun text(): String = text

}