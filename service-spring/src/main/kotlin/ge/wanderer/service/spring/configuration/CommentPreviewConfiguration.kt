package ge.wanderer.service.spring.configuration

import ge.wanderer.common.enums.UserContentType
import ge.wanderer.common.enums.UserContentType.*
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class CommentPreviewConfiguration {

    @Bean
    fun commentPreviewProperties(
        @Value("\${pin.comments.preview.size:3}") previewSizeForPin: Int,
        @Value("\${post.comments.preview.size:3}") previewSizeForPost: Int,
        @Value("\${poll.comments.preview.size:3}") previewSizeForPoll: Int,
        @Value("\${comment.replies.preview.size:3}") previewSizeForComment: Int
    ): Map<UserContentType, Int> =
        mapOf(
            Pair(PIN, previewSizeForPin),
            Pair(POST, previewSizeForPost),
            Pair(POLL, previewSizeForPoll),
            Pair(COMMENT, previewSizeForComment)
        )
}