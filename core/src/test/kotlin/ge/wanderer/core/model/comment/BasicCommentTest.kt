package ge.wanderer.core.model.comment

import ge.wanderer.common.today
import ge.wanderer.core.model.content.Vote
import ge.wanderer.core.model.content.VoteType.*
import ge.wanderer.core.model.content.status.Active
import ge.wanderer.core.model.user.User
import io.mockk.mockk
import org.joda.time.LocalDate
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class BasicCommentTest {

    @Test
    fun isCorrectlyRated() {
        val comment = createNewComment(today(), "Some text", mockk<User>())

        comment.giveVote(Vote(mockk(), 1, UP))
        assertEquals(1, comment.rating())

        comment.giveVote(Vote(mockk(), 2, UP))
        assertEquals(3, comment.rating())

        comment.giveVote(Vote(mockk(), 1, DOWN))
        assertEquals(2, comment.rating())

        comment.giveVote(Vote(mockk(), 3, DOWN))
        assertEquals(-1, comment.rating())
    }

    private fun createNewComment(createDate: LocalDate, text: String, author: User): BasicComment =
        BasicComment(
            author,
            createDate,
            text,
            mutableListOf(),
            mutableListOf(),
            Active(createDate)
        )
}