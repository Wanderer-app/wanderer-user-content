package ge.wanderer.core.command.pin

import ge.wanderer.core.command.Command
import ge.wanderer.core.command.CommandExecutionResult
import ge.wanderer.core.command.success
import ge.wanderer.core.integration.user.User
import ge.wanderer.core.model.map.IPin
import ge.wanderer.core.model.map.RouteElementContent
import java.lang.IllegalStateException

class UpdatePinCommand(
    private val pin: IPin,
    private val newContent: RouteElementContent,
    private val updater: User
): Command<IPin> {
    override fun execute(): CommandExecutionResult<IPin> =
        if (pin.creator() == updater) {
            success("Pin updated", pin.update(newContent))
        } else {
            throw IllegalStateException("You can't update this pin")
        }
}