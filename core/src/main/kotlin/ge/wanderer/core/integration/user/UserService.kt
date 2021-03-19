package ge.wanderer.core.integration.user

import ge.wanderer.core.model.comment.IComment
import ge.wanderer.core.model.content.RateableContent
import ge.wanderer.core.model.content.UserAddedContent
import ge.wanderer.core.model.map.IPin
import ge.wanderer.core.model.rating.IVote

interface UserService {
    fun findUserById(userId: Long)
    fun usersContentWasRated(rateableContent: RateableContent, vote: IVote)
    fun notifyContentStatusChange(content: UserAddedContent)
}