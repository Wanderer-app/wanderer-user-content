package ge.wanderer.core.command.content

import ge.wanderer.core.command.Command
import ge.wanderer.core.command.CommandExecutionResult
import ge.wanderer.core.command.success
import ge.wanderer.core.integration.user.User
import ge.wanderer.core.integration.user.UserService
import ge.wanderer.core.model.content.UserAddedContent
import org.joda.time.LocalDateTime
import java.lang.IllegalStateException

class ActivateContentCommand(
    private val activator: User,
    private val content: UserAddedContent,
    private val onDate: LocalDateTime,
    private val userService: UserService
): Command<UserAddedContent> {

    override fun execute(): CommandExecutionResult<UserAddedContent> {
        content.activate(onDate, activator)
        if (activator.isAdmin) {
            userService.notifyContentStatusChange(content)
        }
        return success("${content.contentType()} activated successfully!", content)
    }



}