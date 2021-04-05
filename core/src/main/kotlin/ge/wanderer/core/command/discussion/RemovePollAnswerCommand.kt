package ge.wanderer.core.command.discussion

import ge.wanderer.core.command.Command
import ge.wanderer.core.command.CommandExecutionResult
import ge.wanderer.core.command.success
import ge.wanderer.core.integration.user.User
import ge.wanderer.core.integration.user.UserService
import ge.wanderer.core.model.discussion.poll.IPoll
import org.joda.time.LocalDateTime

class RemovePollAnswerCommand(
    private val poll: IPoll,
    private val user: User,
    private val answerId: Long,
    private val onDate: LocalDateTime,
    private val userService: UserService
): Command<IPoll> {
    override fun execute(): CommandExecutionResult<IPoll> {
        check(user.isAdmin) { "You dont have rights to remove this answer" }

        val answer = poll.answers().first { it.id() == answerId }
        answer.remove(onDate, user)

        if (user != answer.creator()) {
            userService.notifyContentStatusChange(answer)
        }
        return success("Answer Removed!", poll)
    }
}