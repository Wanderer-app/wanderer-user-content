package ge.wanderer.service.spring.command

import ge.wanderer.core.command.Command
import ge.wanderer.core.command.decorator.ExceptionHandlingCommand
import ge.wanderer.persistence.command.decorator.PersistentCommand
import ge.wanderer.persistence.repository.BaseRepository
import ge.wanderer.service.spring.command.decorator.LoggedCommand
import org.slf4j.Logger
import org.springframework.stereotype.Component

@Component
class CommandProvider {

    fun <T> decorateCommand(command: Command<T>, forModel: T, logger:Logger): Command<T> =
        LoggedCommand(
            TransactionalCommand(
                ExceptionHandlingCommand(command, forModel)
            ),
            command.javaClass.simpleName,
            logger
        )

    fun <T> decoratePersistentCommand(command: Command<T>, forModel: T, repository: BaseRepository<T>, logger: Logger): Command<T> =
        LoggedCommand(
            TransactionalCommand(
                ExceptionHandlingCommand(
                    PersistentCommand(command, repository),
                    forModel
                )
            ),
            command.javaClass.simpleName,
            logger
        )
}