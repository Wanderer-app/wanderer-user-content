package ge.wanderer.persistence.command.decorator

import ge.wanderer.core.command.Command
import ge.wanderer.core.command.CommandExecutionResult
import ge.wanderer.core.command.success
import ge.wanderer.persistence.repository.BaseRepository

class PersistentCommand<T>(
    private val command: Command<T>,
    private val repository: BaseRepository<T>
): Command<T> {
    override fun execute(): CommandExecutionResult<T> {
        val result = command.execute()
        return if (result.isSuccessful) {
            success(
                result.message + ". New model persisted successfully",
                repository.persist(result.returnedModel)
            )
        } else {
            result
        }
    }
}