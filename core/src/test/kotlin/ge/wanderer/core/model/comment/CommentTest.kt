package ge.wanderer.core.model.comment

import ge.wanderer.common.now
import ge.wanderer.common.dateTime
import ge.wanderer.core.model.rating.Vote
import ge.wanderer.core.model.rating.VoteType.*
import ge.wanderer.core.model.content.status.StatusType
import ge.wanderer.core.model.createDownVote
import ge.wanderer.core.model.createNewComment
import ge.wanderer.core.model.createUpVote
import io.mockk.mockk
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class CommentTest {

    @Test
    fun isCorrectlyRated() {
        val comment = createNewComment(1L, now(), "Some text", mockk())

        comment.giveVote(createUpVote(1L, mockk(), now(), 1))
        assertEquals(1, comment.rating())

        comment.giveVote(createUpVote(2L, mockk(), now(), 2))
        assertEquals(3, comment.rating())

        comment.giveVote(createDownVote(2L, mockk(), now(), 1))
        assertEquals(2, comment.rating())

        comment.giveVote(createDownVote(2L, mockk(), now(), 3))
        assertEquals(-1, comment.rating())
    }

    @Test
    fun isCorrectlyRepliedTo() {
        val comment = createNewComment(1L, now(), "Some text", mockk())
        val reply1 = createNewComment(1L, now().plusHours(1), "Reply 1", mockk())
        val reply2 = createNewComment(1L, now().plusHours(2), "Reply 2", mockk())
        val reply3 = createNewComment(1L, now().plusHours(3), "Reply 3", mockk())
        val replyToReply1 = createNewComment(1L, now().plusHours(3), "Reply to reply 1", mockk())

        assertEquals(0, comment.comments().size)

        comment.addComment(reply1)
        assertEquals(1, comment.comments().size)

        comment.addComment(reply2)
        assertEquals(2, comment.comments().size)

        comment.addComment(reply3)
        assertEquals(3, comment.comments().size)

        reply1.addComment(replyToReply1)
        assertEquals(3, comment.comments().size)
        assertEquals(1, comment.comments()[0].comments().size)

        reply2.remove(now())
        assertEquals(2, comment.comments().size)

    }

    @Test
    fun canBeRemovedAndActivated() {
        val banDate = dateTime("2021-03-15T00:00:00")
        val unBanDate = dateTime("2021-03-16T00:00:00")
        val comment = createNewComment(1L, now(), "Inappropriate text", mockk())

        comment.remove(banDate)
        assertEquals(banDate, comment.statusUpdatedAt())
        assertTrue(comment.isRemoved())

        comment.activate(unBanDate)
        assertEquals(unBanDate, comment.statusUpdatedAt())
        assertTrue(comment.isActive())
    }

}