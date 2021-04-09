package ge.wanderer.service.spring.command.decorator

import ge.wanderer.core.command.Command
import ge.wanderer.core.command.CommandExecutionResult
import ge.wanderer.core.model.content.UserAddedContent
import org.slf4j.Logger

class LoggedCommand<T>(
    private val command: Command<T>,
    private val commandName: String,
    private val logger: Logger
): Command<T> {

    override fun execute(): CommandExecutionResult<T> {
        logger.info("Starting execution of command: $commandName")
        val executionResult = command.execute()
        logger.info(finishMessage(executionResult))
        return executionResult
    }

    private fun finishMessage(result: CommandExecutionResult<T>) = """
        Finished execution of command: $commandName Result = {
                Success: ${result.isSuccessful},
                Message: ${result.message},
                Model: ${result.returnedModel.asString()} 
        }"
    """.trimIndent()

    private fun T.asString(): String =
        if (this is UserAddedContent) {
            "${this.contentType()}{id = ${this.id()}}"
        } else {
            this.toString()
        }

}