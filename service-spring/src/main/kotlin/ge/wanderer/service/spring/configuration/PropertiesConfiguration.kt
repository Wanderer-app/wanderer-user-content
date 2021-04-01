package ge.wanderer.service.spring.configuration

import ge.wanderer.common.enums.UserContentType
import ge.wanderer.common.enums.UserContentType.*
import ge.wanderer.core.integration.user.UserService
import ge.wanderer.service.spring.configuration.UserServiceImplementationType.MOCKED
import ge.wanderer.service.spring.configuration.UserServiceImplementationType.REAL
import ge.wanderer.service.spring.integration.user.UserServiceImpl
import ge.wanderer.service.spring.integration.user.UserServiceMockImpl
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class PropertiesConfiguration {

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

    @Bean
    fun reportedContentNotificationProperties(
        @Value("\${post.reporting.reports-number-to-notify:5}") reportsNumberForPostNotification: Int,
        @Value("\${pin.reporting.reports-number-to-notify:5}") reportsNumberForPinNotification: Int,
        @Value("\${comment.reporting.reports-number-to-notify:5}") reportsNumberForCommentNotification: Int
    ): Map<UserContentType, Int> =
        mapOf(
            Pair(PIN, reportsNumberForPinNotification),
            Pair(POST, reportsNumberForPostNotification),
            Pair(COMMENT, reportsNumberForCommentNotification)
        )

    @Bean
    fun reportedContentRemovalProperties(
        @Value("\${post.reporting.reports-number-to-remove:10}") reportsNumberForPostRemoval: Int,
        @Value("\${pin.reporting.reports-number-to-remove:10}") reportsNumberForPinRemoval: Int,
        @Value("\${comment.reporting.reports-number-to-remove:10}") reportsNumberForCommentRemoval: Int
    ): Map<UserContentType, Int> =
        mapOf(
            Pair(PIN, reportsNumberForPinRemoval),
            Pair(POST, reportsNumberForPostRemoval),
            Pair(COMMENT, reportsNumberForCommentRemoval)
        )

    @Bean
    fun userService(
        @Value("\${integration.users.impl-type:MOCKED}") type: UserServiceImplementationType,
        @Autowired realUserService: UserServiceImpl,
        @Autowired mockedUserService: UserServiceMockImpl
    ): UserService =
        when(type) {
            MOCKED -> mockedUserService
            REAL -> realUserService
        }


}