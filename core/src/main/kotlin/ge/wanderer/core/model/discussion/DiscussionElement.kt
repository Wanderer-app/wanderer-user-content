package ge.wanderer.core.model.discussion

import ge.wanderer.core.model.content.UserAddedContent
import ge.wanderer.core.data.file.AttachedFile

interface DiscussionElement: UserAddedContent {
    fun content(): String
    fun attachedFiles(): List<AttachedFile>
    fun routeCode(): String
    fun type(): DiscussionElementType
}