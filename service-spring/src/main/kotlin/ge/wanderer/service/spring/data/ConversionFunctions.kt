package ge.wanderer.service.spring.data

import ge.wanderer.core.model.comment.IComment
import ge.wanderer.core.model.content.RateableContent
import ge.wanderer.service.protocol.data.CommentData
import ge.wanderer.service.protocol.data.RatingData

fun IComment.data(
    repliesPreview: List<CommentData> = listOf()
): CommentData =
    CommentData(this.id(), this.creator(), this.createdAt(), this.statusUpdatedAt(), this.text(), this.rating(), this.comments().size, repliesPreview)

fun RateableContent.ratingData() =
    RatingData(this.rating())