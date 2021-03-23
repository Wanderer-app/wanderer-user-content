package ge.wanderer.core.command.content

import ge.wanderer.core.command.Command
import ge.wanderer.core.command.CommandExecutionResult
import ge.wanderer.core.command.success
import ge.wanderer.core.integration.user.User
import ge.wanderer.core.integration.user.UserService
import ge.wanderer.core.model.content.UserAddedContent
import org.joda.time.LocalDateTime

class RemoveContentCommand<T: UserAddedContent>(
    private val remover: User,
    private val content: T,
    private val onDate: LocalDateTime,
    private val userService: UserService
): Command<T> {

    override fun execute(): CommandExecutionResult<T> {
        content.remove(onDate, remover)
        if (remover.isAdmin) {
            userService.notifyContentStatusChange(content)
        }
        return success("${content.contentType()} removed successfully!", content)
    }


}