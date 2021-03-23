package ge.wanderer.core.command.content

import ge.wanderer.core.command.Command
import ge.wanderer.core.command.CommandExecutionResult
import ge.wanderer.core.command.success
import ge.wanderer.core.integration.user.User
import ge.wanderer.core.integration.user.UserService
import ge.wanderer.core.model.content.UserAddedContent
import org.joda.time.LocalDateTime

class ActivateContentCommand<T: UserAddedContent>(
    private val activator: User,
    private val content: T,
    private val onDate: LocalDateTime,
    private val userService: UserService
): Command<T> {

    override fun execute(): CommandExecutionResult<T> {
        content.activate(onDate, activator)
        if (activator.isAdmin) {
            userService.notifyContentStatusChange(content)
        }
        return success("${content.contentType()} activated successfully!", content)
    }



}