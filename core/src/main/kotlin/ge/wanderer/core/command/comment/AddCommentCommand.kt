package ge.wanderer.core.command.comment

import ge.wanderer.core.command.Command
import ge.wanderer.core.command.CommandExecutionResult
import ge.wanderer.core.command.success
import ge.wanderer.core.integration.user.User
import ge.wanderer.core.integration.user.UserService
import ge.wanderer.core.model.comment.Comment
import ge.wanderer.core.model.content.CommentableContent
import ge.wanderer.core.model.content.status.Active
import ge.wanderer.core.repository.TRANSIENT_ID
import org.joda.time.LocalDateTime

class AddCommentCommand<T: CommentableContent>(
    private val commentText: String,
    private val commentor: User,
    private val onDateTime: LocalDateTime,
    private val commentableContent: T,
    private val userService: UserService
): Command<T> {

    override fun execute(): CommandExecutionResult<T> {
        val comment = Comment(TRANSIENT_ID, commentor, onDateTime, commentText, Active(onDateTime, commentor))
        commentableContent.addComment(comment)
        userService.notifyContentWasCommented(commentableContent, comment)
        return success("Comment added", commentableContent);
    }
}