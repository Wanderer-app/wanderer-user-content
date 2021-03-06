package ge.wanderer.core.command.rating

import ge.wanderer.common.constants.TRANSIENT_ID
import ge.wanderer.core.integration.user.User
import ge.wanderer.core.integration.user.UserService
import ge.wanderer.core.model.content.RateableContent
import ge.wanderer.core.model.content.status.Active
import ge.wanderer.core.model.rating.IVote
import ge.wanderer.core.model.rating.Vote
import ge.wanderer.common.enums.VoteType
import org.joda.time.LocalDateTime

class GiveOnePointCommand(
    private val voteType: VoteType,
    private val voter: User,
    private val voteDate: LocalDateTime,
    rateableContent: RateableContent,
    userService: UserService
): GiveVoteCommand(rateableContent, userService) {

    override fun createVote(): IVote =
        Vote(TRANSIENT_ID, voter, voteDate, Active(voteDate, voter), 1, voteType)

}