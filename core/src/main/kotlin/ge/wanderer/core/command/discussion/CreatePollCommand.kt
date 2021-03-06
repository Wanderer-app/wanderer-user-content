package ge.wanderer.core.command.discussion

import ge.wanderer.common.constants.TRANSIENT_ID
import ge.wanderer.core.command.Command
import ge.wanderer.core.command.CommandExecutionResult
import ge.wanderer.core.command.success
import ge.wanderer.core.integration.user.User
import ge.wanderer.core.model.content.status.Active
import ge.wanderer.core.model.discussion.poll.IPoll
import ge.wanderer.core.model.discussion.poll.IPollAnswer
import ge.wanderer.core.model.discussion.poll.Poll
import ge.wanderer.core.model.discussion.poll.PollAnswer
import org.joda.time.LocalDateTime

class CreatePollCommand(
    private val onDate: LocalDateTime,
    private val user: User,
    private val pollText: String,
    private val routeCode: String,
    private val answersText: Set<String>
) : Command<IPoll> {
    override fun execute(): CommandExecutionResult<IPoll> {

        check(answersText.size >= 2) { "Poll must have at least 2 answers!" }
        check(user.isAdmin) { "Only admin can create polls" }

        val poll = Poll(
            TRANSIENT_ID,
            user, onDate,
            Active(onDate, user),
            routeCode,
            pollText,
            answers(),
            mutableListOf()
        )
        return success("Poll created!", poll)
    }

    private fun answers(): MutableSet<IPollAnswer> =
        answersText
            .map { PollAnswer(TRANSIENT_ID, it, onDate, user, Active(onDate, user), mutableSetOf()) }
            .toMutableSet()
}