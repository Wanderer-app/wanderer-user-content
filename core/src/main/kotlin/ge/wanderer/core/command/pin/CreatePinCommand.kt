package ge.wanderer.core.command.pin

import ge.wanderer.common.constants.TRANSIENT_ID
import ge.wanderer.common.enums.PinType
import ge.wanderer.common.map.LatLng
import ge.wanderer.core.command.Command
import ge.wanderer.core.command.CommandExecutionResult
import ge.wanderer.core.command.success
import ge.wanderer.core.integration.user.User
import ge.wanderer.core.model.content.status.Active
import ge.wanderer.core.model.map.IPin
import ge.wanderer.core.model.map.Pin
import ge.wanderer.core.model.map.PinContent
import org.joda.time.LocalDateTime

class CreatePinCommand(
    private val onDate: LocalDateTime,
    private val user: User,
    private val type: PinType,
    private val content: PinContent,
    private val location: LatLng,
    private val routeCode: String
    ): Command<IPin> {
    override fun execute(): CommandExecutionResult<IPin> {
        val pin = Pin(
            TRANSIENT_ID,
            user,
            onDate,
            location,
            routeCode,
            type, content,
            Active(onDate, user)
        )
        return success("Pin Created", pin)
    }
}