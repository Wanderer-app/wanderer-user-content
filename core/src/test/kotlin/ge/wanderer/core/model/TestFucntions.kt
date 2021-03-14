package ge.wanderer.core.model

import ge.wanderer.common.map.LatLng
import ge.wanderer.core.model.comment.Comment
import ge.wanderer.core.model.content.status.Active
import ge.wanderer.core.model.discussion.post.Post
import ge.wanderer.core.model.map.RouteElementContent
import ge.wanderer.core.model.map.MarkerType
import ge.wanderer.core.model.map.Pin
import ge.wanderer.core.data.user.User
import ge.wanderer.core.model.rating.Vote
import ge.wanderer.core.model.rating.VoteType
import org.joda.time.LocalDateTime

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
    val content = RouteElementContent("aaa", text, null)
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

