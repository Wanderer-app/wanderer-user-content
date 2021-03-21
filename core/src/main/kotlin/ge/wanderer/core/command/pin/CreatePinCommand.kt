package ge.wanderer.core.command.pin

import ge.wanderer.common.map.LatLng
import ge.wanderer.core.command.Command
import ge.wanderer.core.command.CommandExecutionResult
import ge.wanderer.core.command.success
import ge.wanderer.core.integration.user.User
import ge.wanderer.core.model.content.status.Active
import ge.wanderer.core.model.map.IPin
import ge.wanderer.core.model.map.MarkerType
import ge.wanderer.core.model.map.Pin
import ge.wanderer.core.model.map.RouteElementContent
import ge.wanderer.core.repository.TRANSIENT_ID
import org.joda.time.LocalDateTime

class CreatePinCommand(
    private val onDate: LocalDateTime,
    private val user: User,
    private val type: MarkerType,
    private val content: RouteElementContent,
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