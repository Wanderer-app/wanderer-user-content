package ge.wanderer.core.command.discussion

import ge.wanderer.core.command.Command
import ge.wanderer.core.command.CommandExecutionResult
import ge.wanderer.core.command.success
import ge.wanderer.core.integration.user.User
import ge.wanderer.core.model.UpdateDiscussionElementData
import ge.wanderer.core.model.discussion.DiscussionElement
import java.lang.IllegalStateException


class UpdateDiscussionElementCommand(
    private val element: DiscussionElement,
    private val data: UpdateDiscussionElementData,
    private val updater: User
): Command<DiscussionElement> {
    override fun execute(): CommandExecutionResult<DiscussionElement> =
        if (element.creator() == updater) {
            success("Element updated", element.update(data))
        } else {
            throw IllegalStateException("You can't update this element")
        }
}