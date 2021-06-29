package ge.wanderer.persistence.inMemory.support

import ge.wanderer.common.constants.TRANSIENT_ID
import ge.wanderer.common.enums.PinType
import ge.wanderer.common.listing.ListingParams
import ge.wanderer.common.map.LatLng
import ge.wanderer.core.integration.user.User
import ge.wanderer.core.model.comment.Comment
import ge.wanderer.core.model.content.status.Active
import ge.wanderer.core.model.discussion.poll.IPollAnswer
import ge.wanderer.core.model.discussion.poll.Poll
import ge.wanderer.core.model.discussion.poll.PollAnswer
import ge.wanderer.core.model.discussion.post.Post
import ge.wanderer.core.model.map.Pin
import ge.wanderer.core.model.map.PinContent
import org.joda.time.LocalDateTime

fun createNewComment(id: Long, createDate: LocalDateTime, text: String, author: User): Comment =
    Comment(
        id,
        author,
        createDate,
        text,
        Active(createDate, author)
    )

fun createPin(id: Long, type: PinType, user: User, createTime: LocalDateTime, location: LatLng, routeCode: String, text: String): Pin {
    val content = PinContent("Title", text, null)
    return Pin(
        id,
        user,
        createTime,
        location,
        routeCode,
        type,
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


fun jambura(): User = User("1", "Nika", "Jamburia", 10, true)
fun patata(): User = User("2", "Nikoloz", "Patatishvili", 10, false)

fun pollWithAnswers(id: Long, creator: User, createTime: LocalDateTime, routeCode: String, title: String, answers: Set<String>): Poll {
    val pollAnswers: MutableSet<IPollAnswer> = answers
        .map { PollAnswer(TRANSIENT_ID, it, createTime, creator, Active(createTime, creator), mutableSetOf()) }
        .toMutableSet()
    return Poll(id, creator, createTime, Active(createTime, creator), routeCode, title, pollAnswers, mutableListOf())
}

val DEFAULT_LISTING_PARAMS = ListingParams(100, 1, null, listOf())

