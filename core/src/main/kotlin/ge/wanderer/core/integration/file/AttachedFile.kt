package ge.wanderer.core.integration.file

import ge.wanderer.common.enums.FileType

data class AttachedFile (
    val externalId: String,
    val fileType: FileType = FileType.IMAGE
)