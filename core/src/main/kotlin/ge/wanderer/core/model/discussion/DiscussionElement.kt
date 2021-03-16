package ge.wanderer.core.model.discussion

import ge.wanderer.core.model.content.UserAddedContent
import ge.wanderer.core.data.file.AttachedFile
import ge.wanderer.core.model.UpdateDiscussionElementData

interface DiscussionElement: UserAddedContent {
    fun content(): String
    fun update(updateData: UpdateDiscussionElementData): DiscussionElement
    fun attachedFiles(): List<AttachedFile>
    fun routeCode(): String
}