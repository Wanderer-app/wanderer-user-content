package ge.wanderer.core.command.rating

import ge.wanderer.core.command.Command
import ge.wanderer.core.command.CommandExecutionResult
import ge.wanderer.core.command.success
import ge.wanderer.core.model.content.RateableContent
import ge.wanderer.core.model.rating.IVote
import java.lang.IllegalStateException

class GiveVoteCommand(
    private val vote: IVote,
    private val rateableContent: RateableContent
): Command<RateableContent> {
    override fun execute(): CommandExecutionResult<RateableContent> {
        if (vote.creator() == rateableContent.creator()) {
            throw IllegalStateException("Cant vote for your own content!")
        }
        rateableContent.removeVotesBy(vote.creator(), vote.createdAt())
        rateableContent.giveVote(vote)
        return success("Vote added", rateableContent)
    }
}