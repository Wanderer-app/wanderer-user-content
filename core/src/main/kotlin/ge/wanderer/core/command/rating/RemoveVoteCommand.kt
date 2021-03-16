package ge.wanderer.core.command.rating

import ge.wanderer.core.command.Command
import ge.wanderer.core.command.CommandExecutionResult
import ge.wanderer.core.command.success
import ge.wanderer.core.integration.user.User
import ge.wanderer.core.model.content.RateableContent
import org.joda.time.LocalDateTime

class RemoveVoteCommand(
    private val voter: User,
    private val date: LocalDateTime,
    private val rateable: RateableContent
): Command<RateableContent> {
    override fun execute(): CommandExecutionResult<RateableContent> {
        rateable.removeVotesBy(voter, date)
        return success("Vote Removed", rateable)
    }
}