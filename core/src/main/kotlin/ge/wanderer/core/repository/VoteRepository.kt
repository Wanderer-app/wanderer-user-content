package ge.wanderer.core.repository

import ge.wanderer.core.model.comment.IComment
import ge.wanderer.core.model.content.UserAddedContent
import ge.wanderer.core.model.rating.IVote

interface VoteRepository: BaseRepository<IVote> {
    fun listByContent(content: UserAddedContent): List<IComment>
}