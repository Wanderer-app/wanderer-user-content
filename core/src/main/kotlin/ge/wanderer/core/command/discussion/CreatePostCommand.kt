package ge.wanderer.core.command.discussion

import ge.wanderer.common.constants.TRANSIENT_ID
import ge.wanderer.core.command.Command
import ge.wanderer.core.command.CommandExecutionResult
import ge.wanderer.core.command.success
import ge.wanderer.core.data.file.AttachedFile
import ge.wanderer.core.integration.user.User
import ge.wanderer.core.model.content.status.Active
import ge.wanderer.core.model.discussion.post.IPost
import ge.wanderer.core.model.discussion.post.Post
import org.joda.time.LocalDateTime

class CreatePostCommand(
    private val onDate: LocalDateTime,
    private val user: User,
    private val routeCode: String,
    private val text: String,
    private val attachedFiles: List<AttachedFile>
) : Command<IPost> {
    override fun execute(): CommandExecutionResult<IPost> =
        success(
            "Post created!",
            Post(TRANSIENT_ID, user, onDate, text, routeCode, attachedFiles, Active(onDate, user))
        )

}
