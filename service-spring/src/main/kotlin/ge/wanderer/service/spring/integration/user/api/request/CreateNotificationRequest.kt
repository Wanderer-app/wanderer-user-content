package ge.wanderer.service.spring.integration.user.api.request

import ge.wanderer.common.enums.UserContentType

data class CreateNotificationRequest (
    val type: NotificationType,
    val sender_username: String,
    val reciever_username: String,
    val redirect_url: String,
    val created_at: String,
    val related_content_type: UserContentType
)

enum class NotificationType {
    COMMENT,
    REPLY,
    RATING,
    CONTENT_STATUS_CHANGE
}

enum class NotificationStatus {
    SEEN,
    UNSEEN,
    OPENED,
}