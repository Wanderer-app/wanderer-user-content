package ge.wanderer.service.protocol.request

import ge.wanderer.common.enums.PinType
import ge.wanderer.common.map.LatLng
import ge.wanderer.service.protocol.data.FileData
import org.joda.time.LocalDateTime

data class CreatePinRequest (
    val onDate: LocalDateTime,
    val userId: String,
    val type: PinType,
    val title: String,
    val text: String,
    val attachedFile: FileData?,
    val location: LatLng,
    val routeCode: String
)

data class UpdatePinRequest(
    val pinId: Long,
    val newTitle: String,
    val newText: String,
    val newFile: FileData?,
    val updaterId: String
)