package ge.wanderer.core.command.comment

import ge.wanderer.core.command.Command
import ge.wanderer.core.command.CommandExecutionResult
import ge.wanderer.core.command.success
import ge.wanderer.core.integration.user.User
import ge.wanderer.core.model.UpdateCommentData
import ge.wanderer.core.model.comment.IComment

class UpdateCommentCommand(
    private val comment: IComment,
    private val data: UpdateCommentData,
    private val updater: User
): Command<IComment> {
    override fun execute(): CommandExecutionResult<IComment> =
        if (comment.creator() == updater) {
            success("Comment updated", comment.update(data))
        } else {
            throw IllegalStateException("You can't update this comment")
        }

}