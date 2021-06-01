package ge.wanderer.core.model

import ge.wanderer.core.integration.file.AttachedFile

data class UpdateCommentData(val text: String)

data class UpdateDiscussionElementData(val contentToUpdate: String, val files: List<AttachedFile>)