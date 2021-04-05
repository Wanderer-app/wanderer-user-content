package ge.wanderer.service.spring.command

import ge.wanderer.core.command.Command
import ge.wanderer.core.command.decorator.ExceptionHandlingCommand
import ge.wanderer.persistence.command.decorator.PersistentCommand
import ge.wanderer.persistence.repository.BaseRepository
import org.springframework.stereotype.Component

@Component
class CommandProvider {

    fun <T> decorateCommand(command: Command<T>, forModel: T): Command<T> =
        TransactionalCommand(
            ExceptionHandlingCommand(command, forModel)
        )

    fun <T> decoratePersistentCommand(command: Command<T>, forModel: T, repository: BaseRepository<T>): Command<T> =
        TransactionalCommand(
            ExceptionHandlingCommand(
                PersistentCommand(command, repository),
                forModel
            )
        )
}