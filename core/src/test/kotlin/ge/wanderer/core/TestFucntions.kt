package ge.wanderer.core.model

import ge.wanderer.common.map.LatLng
import ge.wanderer.core.model.comment.Comment
import ge.wanderer.core.model.content.status.Active
import ge.wanderer.core.model.discussion.post.Post
import ge.wanderer.core.model.map.RouteElementContent
import ge.wanderer.core.model.map.MarkerType
import ge.wanderer.core.model.map.Pin
import ge.wanderer.core.integration.user.User
import ge.wanderer.core.model.discussion.poll.PollAnswer
import ge.wanderer.core.model.rating.Vote
import ge.wanderer.core.model.rating.VoteType
import io.mockk.mockk
import org.joda.time.LocalDateTime
import java.net.URL

fun createNewComment(id: Long, createDate: LocalDateTime, text: String, author: User): Comment =
    Comment(
        id,
        author,
        createDate,
        text,
        mutableListOf(),
        mutableListOf(),
        Active(createDate)
    )

fun createTipPin(id: Long, user: User, createTime: LocalDateTime, location: LatLng, routeCode: String, text: String): Pin {
    val content = RouteElementContent("Title", text, null)
    return Pin(
        id,
        user,
        createTime,
        location,
        routeCode,
        MarkerType.TIP,
        content,
        Active(createTime),
        mutableListOf(),
        mutableListOf()
    )
}

fun createNewPostWithoutFiles(id: Long, user: User, content: String, createDate: LocalDateTime): Post =
    Post(
        id,
        user,
        createDate,
        content,
        "123",
        listOf(),
        Active(createDate),
        mutableListOf(),
        mutableListOf()
    )

fun createUpVote(id: Long, user: User, date: LocalDateTime, value: Int) = Vote(id, user, date, Active(date), value, VoteType.UP)
fun createDownVote(id: Long, user: User, date: LocalDateTime, value: Int) = Vote(id, user, date, Active(date), value, VoteType.DOWN)

fun jambura(): User = User(1, "Nika", "Jamburia", 10, true)
fun patata(): User = User(2, "Nika", "Patatishvili", 5, true)
fun jangula(): User = User(3, "Nika", "Jangulashvili", 5, true)

fun pollAnswer(id: Long, answerText: String, createTime: LocalDateTime, answerers: MutableSet<User>): PollAnswer {
    return PollAnswer(
        id,
        answerText,
        createTime,
        mockk(),
        Active(createTime),
        answerers
    )
}

fun Any.getResourceFile(fileName: String): URL =
    this::class.java.classLoader.getResource(fileName)!!

