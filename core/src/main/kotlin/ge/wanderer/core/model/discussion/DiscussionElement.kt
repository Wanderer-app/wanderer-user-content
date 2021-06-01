package ge.wanderer.core.model.discussion

import ge.wanderer.core.integration.file.AttachedFile
import ge.wanderer.core.model.UpdateDiscussionElementData
import ge.wanderer.core.model.content.CommentableContent

interface DiscussionElement: CommentableContent {
    fun content(): String
    fun update(updateData: UpdateDiscussionElementData)
    fun attachedFiles(): List<AttachedFile>
    fun routeCode(): String
}