package ge.wanderer.core.command

interface Command<T> {
    fun execute(): CommandExecutionResult<T>
}