package ge.wanderer.core.model.comment

import ge.wanderer.core.model.UpdateCommentData
import ge.wanderer.core.model.content.CommentableContent
import ge.wanderer.core.model.content.RateableContent
import ge.wanderer.core.model.content.ReportableContent

interface IComment: RateableContent, CommentableContent, ReportableContent {
    fun text(): String
    fun update(updateData: UpdateCommentData)
}