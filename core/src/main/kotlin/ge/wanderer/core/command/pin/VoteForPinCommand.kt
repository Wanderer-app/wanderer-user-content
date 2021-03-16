package ge.wanderer.core.command.pin

import ge.wanderer.core.command.Command
import ge.wanderer.core.command.CommandExecutionResult
import ge.wanderer.core.command.rating.GiveVoteCommand
import ge.wanderer.core.command.success
import ge.wanderer.core.integration.user.User
import ge.wanderer.core.integration.user.UserService
import ge.wanderer.core.model.content.status.Active
import ge.wanderer.core.model.map.IPin
import ge.wanderer.core.model.rating.Vote
import ge.wanderer.core.model.rating.VoteType
import ge.wanderer.core.repository.TRANSIENT_ID
import org.joda.time.LocalDateTime

class VoteForPinCommand(
    private val voteType: VoteType,
    private val voter: User,
    private val voteDate: LocalDateTime,
    private val pin: IPin,
    private val userService: UserService
): Command<IPin> {
    override fun execute(): CommandExecutionResult<IPin> {
        val vote = Vote(TRANSIENT_ID, voter, voteDate, Active(voteDate), voter.pinVoteWeight, voteType)
        val giveVoteResult = GiveVoteCommand(vote, pin).execute()

        userService.usersPinWasRated(pin, vote)
        return success(giveVoteResult.message, pin)
    }

}