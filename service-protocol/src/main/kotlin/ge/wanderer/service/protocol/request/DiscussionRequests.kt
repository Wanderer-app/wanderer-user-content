package ge.wanderer.service.protocol.request

import ge.wanderer.service.protocol.data.FileData
import org.joda.time.LocalDateTime

data class CreatePollRequest (
    val onDate: LocalDateTime,
    val userId: Long,
    val routeCode: String,
    val text: String,
    val answers: Set<String>
)

data class CreatePostRequest (
    val onDate: LocalDateTime,
    val userId: Long,
    val routeCode: String,
    val text: String,
    val attachedFiles: List<FileData>
)

data class UpdatePollRequest (
    val pollId: Long,
    val newText: String,
    val updaterId: Long
)

data class UpdatePostRequest (
    val postId: Long,
    val newText: String,
    val files: List<FileData>,
    val updaterId: Long
)

data class AddAnswerToPollRequest(
    val pollId: Long,
    val userId: Long,
    val answerText: String,
    val date: LocalDateTime
)

data class RemovePollAnswerRequest(
    val pollId: Long,
    val answerId: Long,
    val userId: Long,
    val date: LocalDateTime
)

data class SelectPollAnswerRequest(
    val pollId: Long,
    val userId: Long,
    val answerId: Long
)