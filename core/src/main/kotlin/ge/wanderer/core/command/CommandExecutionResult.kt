package ge.wanderer.core.command

data class CommandExecutionResult<T> (
    val isSuccessful: Boolean,
    val message: String,
    val returnedModel: T
)
fun <T> success(message: String, containedEntity: T): CommandExecutionResult<T> =
    CommandExecutionResult(true, message, containedEntity)

fun <T> fail(message: String, containedEntity: T): CommandExecutionResult<T>  =
    CommandExecutionResult(false, message, containedEntity)
