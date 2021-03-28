package ge.wanderer.service.protocol.request

import ge.wanderer.core.data.file.AttachedFile
import ge.wanderer.core.integration.user.User
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
    val attachedFiles: List<AttachedFile>
)

data class UpdatePollRequest (
    val pollId: Long,
    val newText: String,
    val updaterId: Long
)

data class UpdatePostRequest (
    val postId: Long,
    val newText: String,
    val files: List<AttachedFile>,
    val updaterId: Long
)

data class AddAnswerToPollRequest(
    val pollId: Long,
    val userId: Long,
    val answerText: String,
    val date: LocalDateTime
)

data class SelectPollAnswerRequest(
    val pollId: Long,
    val userId: Long,
    val answerId: Long
)