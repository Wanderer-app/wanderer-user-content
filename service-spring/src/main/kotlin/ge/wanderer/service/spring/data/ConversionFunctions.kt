package ge.wanderer.service.spring.data

import ge.wanderer.core.model.comment.IComment
import ge.wanderer.core.model.discussion.DiscussionElement
import ge.wanderer.core.model.map.IPin
import ge.wanderer.service.protocol.data.CommentData
import ge.wanderer.service.protocol.data.DiscussionElementData
import ge.wanderer.service.protocol.data.PinData
import ge.wanderer.service.protocol.data.PinMapData

fun IComment.data(
    repliesPreview: List<CommentData> = listOf()
): CommentData =
    CommentData(id(), creator(), createdAt(), statusUpdatedAt(), text(), rating(), isActive(), isRemoved(), comments().size, repliesPreview)

fun DiscussionElement.data(
    commentsPreview: List<CommentData> = listOf()
): DiscussionElementData =
    DiscussionElementData(
        id(),
        creator(),
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
        contentType()
    )

fun IPin.data(commentsPreview: List<CommentData> = listOf()): PinData =
    PinData(id(), creator(), createdAt(), statusUpdatedAt(),
        isActive(), isRemoved(), isRelevant(), ratingData(),
        comments().size, commentsPreview, routeCode(), content(), type())

fun IPin.mapData(): PinMapData = PinMapData(id(), routeCode(), location(), type(), createdAt(), content().title, rating())
