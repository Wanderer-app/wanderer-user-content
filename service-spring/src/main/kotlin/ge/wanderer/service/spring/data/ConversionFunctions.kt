package ge.wanderer.service.spring.data

import ge.wanderer.core.integration.file.AttachedFile
import ge.wanderer.core.integration.user.User
import ge.wanderer.core.model.comment.IComment
import ge.wanderer.core.model.content.RateableContent
import ge.wanderer.core.model.discussion.DiscussionElement
import ge.wanderer.core.model.map.IPin
import ge.wanderer.common.enums.VoteType
import ge.wanderer.core.model.report.Report
import ge.wanderer.service.protocol.data.*

fun IComment.data(
    user: User,
    repliesPreview: List<CommentData> = listOf()
): CommentData =
    CommentData(id(), creator().data(), createdAt(), statusUpdatedAt(), text(), rating(), isActive(), isRemoved(), comments().size, repliesPreview, this.getVoteBy(user)?.type() ?: let { null })

fun DiscussionElement.data(
    commentsPreview: List<CommentData> = listOf(),
    requestingUser: User
): DiscussionElementData =
    DiscussionElementData(
        id(),
        creator().data(),
        createdAt(),
        statusUpdatedAt(),
        isActive(),
        isRemoved(),
        ratingData(),
        commentsPreview,
        comments().size,
        routeCode(),
        content(),
        attachedFiles(),
        contentType(),
        userVoteType(requestingUser)
    )

fun DiscussionElement.userVoteType(user: User): VoteType? =
    if (this is RateableContent) {
        this.getVoteBy(user)?.type() ?: let { null }
    } else {
        null
    }

fun IPin.data(commentsPreview: List<CommentData> = listOf(), user: User): PinData =
    PinData(id(), creator().data(), createdAt(), statusUpdatedAt(),
        isActive(), isRemoved(), isRelevant(), ratingData(),
        comments().size, commentsPreview, routeCode(), content().title,
        content().text, content().attachedFile?.data() ?:let { null }, type(),
        location(), getVoteBy(user)?.type() ?: let { null }
    )

fun IPin.mapData(): PinMapData = PinMapData(id(), routeCode(), location(), type(), createdAt(), content().title, rating())

fun User.data(): UserData = UserData(id, firstName, lastName, isAdmin)
fun AttachedFile.data(): FileData = FileData(externalId, fileType)

fun Report.data() = ReportData(id, creator, reportTime, reason)