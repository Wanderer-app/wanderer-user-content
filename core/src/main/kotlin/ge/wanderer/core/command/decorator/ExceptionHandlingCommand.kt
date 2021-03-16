package ge.wanderer.core.command.decorator

import ge.wanderer.core.command.Command
import ge.wanderer.core.command.CommandExecutionResult
import ge.wanderer.core.command.fail

import java.io.PrintWriter
import java.io.StringWriter

class ExceptionHandlingCommand<T>(
    private val command: Command<T>,
    private val model: T
) : Command<T> {
    override fun execute(): CommandExecutionResult<T> =
        try {
            command.execute()
        } catch (e: Exception) {
            fail(e.message ?: "Exception occured: ${e.stackTraceString()}", model)
        }
}

private fun Exception.stackTraceString(): String {
    val sw = StringWriter()
    val pw = PrintWriter(sw)
    this.printStackTrace(pw)
    return sw.toString()
}