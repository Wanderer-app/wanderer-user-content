package ge.wanderer.core.model.content

import ge.wanderer.core.integration.user.User
import ge.wanderer.core.model.rating.IVote
import org.joda.time.LocalDateTime

interface RateableContent: UserAddedContent {
    fun giveVote(vote: IVote)
    fun rating(): Int
    fun removeVotesBy(user: User, onDate: LocalDateTime)
    fun getVoteBy(user: User): IVote?
}