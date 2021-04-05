package ge.wanderer.core.command.pin

import ge.wanderer.common.constants.TRANSIENT_ID
import ge.wanderer.core.command.rating.GiveVoteCommand
import ge.wanderer.core.integration.user.User
import ge.wanderer.core.integration.user.UserService
import ge.wanderer.core.model.content.status.Active
import ge.wanderer.core.model.map.IPin
import ge.wanderer.core.model.rating.IVote
import ge.wanderer.core.model.rating.Vote
import ge.wanderer.core.model.rating.VoteType
import org.joda.time.LocalDateTime

class VoteForPinCommand(
    private val voteType: VoteType,
    private val voter: User,
    private val voteDate: LocalDateTime,
    pin: IPin,
    userService: UserService
): GiveVoteCommand(pin, userService) {

    override fun createVote(): IVote =
        Vote(TRANSIENT_ID, voter, voteDate, Active(voteDate, voter), voter.pinVoteWeight, voteType)


}