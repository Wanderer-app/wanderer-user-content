package ge.wanderer.core.command.comment

import ge.wanderer.core.command.Command
import ge.wanderer.core.command.CommandExecutionResult
import ge.wanderer.core.command.success
import ge.wanderer.core.model.comment.IComment
import ge.wanderer.core.model.content.CommentableContent

class AddCommentCommand(
    private val comment: IComment,
    private val commentableContent: CommentableContent
): Command<CommentableContent> {

    override fun execute(): CommandExecutionResult<CommentableContent> {
        commentableContent.addComment(comment)
        return success("Comment added", commentableContent);
    }
}