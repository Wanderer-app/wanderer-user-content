package ge.wanderer.core.command.decorator

import ge.wanderer.common.functions.asStandardMessage
import ge.wanderer.core.command.Command
import ge.wanderer.core.command.CommandExecutionResult
import ge.wanderer.core.command.fail

class ExceptionHandlingCommand<T>(
    private val command: Command<T>,
    private val model: T
) : Command<T> {
    override fun execute(): CommandExecutionResult<T> =
        try {
            command.execute()
        } catch (e: Exception) {
            fail(e.asStandardMessage(), model)
        }
}
