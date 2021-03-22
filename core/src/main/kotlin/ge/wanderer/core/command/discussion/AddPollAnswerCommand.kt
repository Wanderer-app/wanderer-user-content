package ge.wanderer.core.command.discussion

import ge.wanderer.core.command.Command
import ge.wanderer.core.command.CommandExecutionResult
import ge.wanderer.core.command.success
import ge.wanderer.core.integration.user.User
import ge.wanderer.core.model.content.status.Active
import ge.wanderer.core.model.discussion.poll.IPoll
import ge.wanderer.core.model.discussion.poll.PollAnswer
import ge.wanderer.core.repository.TRANSIENT_ID
import org.joda.time.LocalDateTime

class AddPollAnswerCommand(
    private val poll: IPoll,
    private val answerText: String,
    private val onDate: LocalDateTime,
    private val user: User
): Command<IPoll> {
    override fun execute(): CommandExecutionResult<IPoll> {
        check(user.isAdmin) { "Only administrators can add new answers to polls" }

        val answer = PollAnswer(TRANSIENT_ID, answerText, onDate, user, Active(onDate, user), mutableSetOf())
        poll.addAnswer(answer)
        return success("Answer added!", poll)
    }

}