package ge.wanderer.core.command.discussion

import ge.wanderer.core.command.Command
import ge.wanderer.core.command.CommandExecutionResult
import ge.wanderer.core.command.success
import ge.wanderer.core.integration.user.User
import ge.wanderer.core.model.UpdateDiscussionElementData
import ge.wanderer.core.model.discussion.DiscussionElement


class UpdateDiscussionElementCommand(
    private val element: DiscussionElement,
    private val data: UpdateDiscussionElementData,
    private val updater: User
): Command<DiscussionElement> {
    override fun execute(): CommandExecutionResult<DiscussionElement> {
        check(element.creator() == updater) { "You can't update this element" }
        return success("Element updated", element.update(data))
    }
}