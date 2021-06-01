package ge.wanderer.service.protocol.data

import ge.wanderer.common.enums.FileType

data class FileData(
    val externalId: String,
    val fileType: FileType
)