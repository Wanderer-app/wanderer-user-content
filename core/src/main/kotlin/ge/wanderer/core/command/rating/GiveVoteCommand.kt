package ge.wanderer.core.command.rating

import ge.wanderer.core.command.Command
import ge.wanderer.core.command.CommandExecutionResult
import ge.wanderer.core.command.success
import ge.wanderer.core.integration.user.UserService
import ge.wanderer.core.model.content.RateableContent
import ge.wanderer.core.model.rating.IVote

abstract class GiveVoteCommand(
    private val rateableContent: RateableContent,
    private val userService: UserService
): Command<RateableContent> {
    override fun execute(): CommandExecutionResult<RateableContent> {
        val vote = createVote()
        check(vote.creator() != rateableContent.creator()) { "Cant vote for your own content!" }
        rateableContent.removeVotesBy(vote.creator(), vote.createdAt())
        rateableContent.giveVote(vote)
        userService.usersContentWasRated(rateableContent, vote)
        return success("Vote added", rateableContent)
    }

    abstract fun createVote(): IVote
}