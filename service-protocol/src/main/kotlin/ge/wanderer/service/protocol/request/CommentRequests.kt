package ge.wanderer.service.protocol.request

data class UpdateCommentRequest(
    val commentId: Long,
    val updaterId: Long,
    val text: String
)