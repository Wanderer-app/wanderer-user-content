package ge.wanderer.core.command

data class CommandExecutionResult<T> (
    val isSuccessful: Boolean,
    val message: String,
    val returnedModel: T
) {
    companion object Factory {
        fun <T> success(message: String, containedEntity: T): CommandExecutionResult<T> =
            CommandExecutionResult<T>(true, message, containedEntity)

        fun <T> fail(message: String, containedEntity: T): CommandExecutionResult<T>  =
            CommandExecutionResult<T>(false, message, containedEntity)
    }
}