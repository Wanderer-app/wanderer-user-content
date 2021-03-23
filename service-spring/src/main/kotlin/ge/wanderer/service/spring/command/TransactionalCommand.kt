package ge.wanderer.service.spring.command

import ge.wanderer.core.command.Command
import ge.wanderer.core.command.CommandExecutionResult
import org.springframework.transaction.annotation.Transactional

@Transactional
class TransactionalCommand<T>(
    private val command: Command<T>
): Command<T> {
    override fun execute(): CommandExecutionResult<T> = command.execute()
}