package ge.wanderer.core.command.discussion

import ge.wanderer.core.command.Command
import ge.wanderer.core.command.CommandExecutionResult
import ge.wanderer.core.command.success
import ge.wanderer.core.integration.user.User
import ge.wanderer.core.model.UpdateDiscussionElementData
import ge.wanderer.core.model.discussion.DiscussionElement


class UpdateDiscussionElementCommand<T: DiscussionElement>(
    private val element: T,
    private val data: UpdateDiscussionElementData,
    private val updater: User
): Command<T> {
    override fun execute(): CommandExecutionResult<T> {
        check(element.creator() == updater) { "You can't update this element" }
        return success("${element.contentType()} updated", element.update(data) as T)
    }
}