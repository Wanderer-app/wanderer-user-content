package ge.wanderer.core.command.decorator

import ge.wanderer.core.command.Command
import ge.wanderer.core.command.CommandExecutionResult
import ge.wanderer.core.command.CommandExecutionResult.Factory.fail

class IllegalStateHandlingCommand<T>(
    private val command: Command<T>,
    private val model: T
) : Command<T> {
    override fun execute(): CommandExecutionResult<T> =
        try {
            command.execute()
        } catch (e: IllegalStateException) {
            fail(e.message ?: "Could not execute command", model)
        }

}