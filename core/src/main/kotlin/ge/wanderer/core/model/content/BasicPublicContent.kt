package ge.wanderer.core.model.content

import ge.wanderer.core.model.comment.Comment
import ge.wanderer.core.model.content.status.PublicContentStatus
import ge.wanderer.core.model.user.User
import org.joda.time.LocalDate

abstract class BasicPublicContent(
    private val creator: User,
    private val createdAt: LocalDate,
    private var status: PublicContentStatus,
    private val comments: MutableList<Comment>,
    private val votes: MutableList<Vote>
): PublicContent {

    override fun rating(): Int = votes.map { it.value() }.sum()
    override fun comments(): MutableList<Comment> = comments
    override fun status(): PublicContentStatus = status
    override fun creator(): User = creator
    override fun createdAt(): LocalDate = createdAt

    override fun ban(onDate: LocalDate) {
        status = status.ban(onDate)
    }

    override fun unBan(onDate: LocalDate) {
        status = status.unBan(onDate)
    }

    override fun delete(onDate: LocalDate) {
        status = status.delete(onDate)
    }

    override fun markIrrelevant(onDate: LocalDate) {
        status = status.markIrrelevant(onDate)
    }

    override fun giveVote(vote: Vote) {
        votes.add(vote)
    }

    override fun addComment(comment: Comment) {
        comment.addComment(comment)
    }
}