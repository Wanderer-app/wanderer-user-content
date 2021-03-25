package ge.wanderer.service.spring.data

import ge.wanderer.core.model.comment.IComment
import ge.wanderer.core.model.discussion.DiscussionElement
import ge.wanderer.service.protocol.data.CommentData
import ge.wanderer.service.protocol.data.DiscussionElementData

fun IComment.data(
    repliesPreview: List<CommentData> = listOf()
): CommentData =
    CommentData(this.id(), this.creator(), this.createdAt(), this.statusUpdatedAt(), this.text(), this.rating(), this.isActive(), this.isRemoved(), this.comments().size, repliesPreview)

fun DiscussionElement.data(
    commentsPreview: List<CommentData> = listOf()
): DiscussionElementData =
    DiscussionElementData(
        this.id(),
        this.creator(),
        this.createdAt(),
        this.statusUpdatedAt(),
        this.isActive(),
        this.isRemoved(),
        this.ratingData(),
        commentsPreview,
        this.comments().size,
        this.routeCode(),
        this.content(),
        this.attachedFiles(),
        this.contentType()
    )