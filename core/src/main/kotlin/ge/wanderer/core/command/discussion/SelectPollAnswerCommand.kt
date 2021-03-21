package ge.wanderer.core.command.discussion

import ge.wanderer.core.command.Command
import ge.wanderer.core.command.CommandExecutionResult
import ge.wanderer.core.command.success
import ge.wanderer.core.integration.user.User
import ge.wanderer.core.model.discussion.poll.IPoll

class SelectPollAnswerCommand(
    private val poll: IPoll,
    private val answerId: Long,
    private val answerer: User
): Command<IPoll> {
    override fun execute(): CommandExecutionResult<IPoll> {
        poll.selectAnswer(answerId, answerer)
        return success("Answer Selected", poll)
    }
}