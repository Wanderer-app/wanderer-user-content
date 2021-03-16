package ge.wanderer.core.command.pin

import ge.wanderer.core.command.Command
import ge.wanderer.core.command.CommandExecutionResult
import ge.wanderer.core.command.success
import ge.wanderer.core.integration.user.UserService
import ge.wanderer.core.model.map.IPin
import org.joda.time.LocalDateTime

class MarkPinAsIrrelevantCommand(
    private val pin: IPin,
    private val onDate: LocalDateTime,
    private val userService: UserService
): Command<IPin> {

    override fun execute(): CommandExecutionResult<IPin> {
        pin.markIrrelevant(onDate)
        userService.notifyContentStatusChange(pin)
        return success("Successfully marked as irrelevant", pin)
    }
}