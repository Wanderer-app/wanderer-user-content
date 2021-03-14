package ge.wanderer.core.model.map

import ge.wanderer.core.data.file.AttachedFile

data class RouteElementContent (
    val title: String,
    val text: String,
    val attachedFile: AttachedFile?
)