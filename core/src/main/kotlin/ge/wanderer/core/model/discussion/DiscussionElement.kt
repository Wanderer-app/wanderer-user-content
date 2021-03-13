package ge.wanderer.core.model.discussion

import ge.wanderer.core.model.file.AttachedFile

interface DiscussionElement {
    fun content(): String
    fun attachedFiles(): List<AttachedFile>
}