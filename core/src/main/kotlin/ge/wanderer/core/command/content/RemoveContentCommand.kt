package ge.wanderer.core.command.content

import ge.wanderer.core.command.Command
import ge.wanderer.core.command.CommandExecutionResult
import ge.wanderer.core.command.success
import ge.wanderer.core.integration.user.User
import ge.wanderer.core.integration.user.UserService
import ge.wanderer.core.model.content.UserAddedContent
import org.joda.time.LocalDateTime
import java.lang.IllegalStateException

class RemoveContentCommand(
    private val content: UserAddedContent,
    private val onDate: LocalDateTime,
    private val remover: User,
    private val userService: UserService
): Command<UserAddedContent> {

    override fun execute(): CommandExecutionResult<UserAddedContent> =
        if (remover == content.creator() || remover.isAdmin) {
            content.remove(onDate)
            if (remover.isAdmin) {
                userService.notifyContentStatusChange(content)
            }
            success("${content.contentType()} removed successfully!", content)
        } else {
            throw IllegalStateException("You dont have rights to remove this comment")
        }

}