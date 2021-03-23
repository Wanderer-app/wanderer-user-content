package ge.wanderer.service.spring

import ge.wanderer.common.map.LatLng
import ge.wanderer.core.model.comment.Comment
import ge.wanderer.core.model.content.status.Active
import ge.wanderer.core.model.discussion.post.Post
import ge.wanderer.core.model.map.RouteElementContent
import ge.wanderer.core.model.map.MarkerType
import ge.wanderer.core.model.map.Pin
import ge.wanderer.core.integration.user.User
import ge.wanderer.core.integration.user.UserService
import ge.wanderer.core.model.discussion.poll.IPollAnswer
import ge.wanderer.core.model.discussion.poll.Poll
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
        Active(createDate, author)
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
        Active(createTime, user)
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
        Active(createDate, user)
    )

fun createUpVote(id: Long, user: User, date: LocalDateTime, value: Int) = Vote(id, user, date, Active(date, user), value, VoteType.UP)
fun createDownVote(id: Long, user: User, date: LocalDateTime, value: Int) = Vote(id, user, date, Active(date, user), value, VoteType.DOWN)

fun jambura(): User = User(1, "Nika", "Jamburia", 10, true)
fun patata(): User = User(2, "Nika", "Patatishvili", 5, true)
fun jangula(): User = User(3, "Nika", "Jangulashvili", 5, true)
fun vipiSoxumski(): User = User(4, "Vipi", "Soxumski", 1, false)
fun kalduna(): User = User(5, "Kalduna", "Kalduna", 10, false)

fun pollAnswer(id: Long, answerText: String, createTime: LocalDateTime, answerers: MutableSet<User>, creator: User): PollAnswer {
    return PollAnswer(
        id,
        answerText,
        createTime,
        creator,
        Active(createTime, creator),
        answerers
    )
}

fun Any.getResourceFile(fileName: String): URL =
    this::class.java.classLoader.getResource(fileName)!!

fun createPoll(id: Long, creator: User, createTime: LocalDateTime, routeCode: String, title: String, answers: MutableSet<IPollAnswer>) =
    Poll(id, creator, createTime, Active(createTime, creator), routeCode, title, answers, mutableListOf())

fun Poll.addAnswer(id: Long, text: String) {
    val answer = PollAnswer(id, text, this.createdAt(), this.creator(), Active(this.createdAt(), this.creator()), mutableSetOf())
    this.addAnswer(answer)
}

