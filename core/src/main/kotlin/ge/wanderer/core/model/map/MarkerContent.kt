package ge.wanderer.core.model.map

import ge.wanderer.core.model.file.AttachedFile

data class MarkerContent (
    val title: String,
    val text: String,
    val attachedFile: AttachedFile
)