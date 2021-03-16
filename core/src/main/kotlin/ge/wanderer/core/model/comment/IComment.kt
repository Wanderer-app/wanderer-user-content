package ge.wanderer.core.model.comment

import ge.wanderer.core.model.UpdateCommentData
import ge.wanderer.core.model.content.CommentableContent
import ge.wanderer.core.model.content.UserAddedContent
import ge.wanderer.core.model.content.RateableContent

interface IComment: RateableContent, CommentableContent {
    fun text(): String
    fun update(updateData: UpdateCommentData): IComment
}