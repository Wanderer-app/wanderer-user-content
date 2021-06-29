package ge.wanderer.service.spring.integration.user.service

import ge.wanderer.common.functions.fromJson
import ge.wanderer.common.functions.toJson
import ge.wanderer.core.integration.user.User
import ge.wanderer.core.integration.user.UserService
import ge.wanderer.core.model.comment.IComment
import ge.wanderer.core.model.content.CommentableContent
import ge.wanderer.core.model.content.RateableContent
import ge.wanderer.core.model.content.ReportableContent
import ge.wanderer.core.model.content.UserAddedContent
import ge.wanderer.core.model.discussion.DiscussionElement
import ge.wanderer.core.model.map.IPin
import ge.wanderer.core.model.rating.IVote
import ge.wanderer.service.spring.integration.user.api.RequireOkResponse
import ge.wanderer.service.spring.integration.user.api.request.CreateNotificationRequest
import ge.wanderer.service.spring.integration.user.api.request.NotificationType
import ge.wanderer.service.spring.integration.user.api.response.GetUserResponse
import org.http4k.core.*
import org.http4k.filter.DebuggingFilters
import org.joda.time.LocalDateTime
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class UserServiceImpl(
    @Autowired
    private val httpClient: HttpHandler
): UserService {

    override fun findUserById(userId: String): User {
        val client = RequireOkResponse().then(httpClient)
        val response = client(Request(Method.GET, "/get_user").query("id", userId))
        val userResponse: GetUserResponse = fromJson(response.bodyString())
        return userFromResponse(userResponse)
    }


    override fun usersContentWasRated(rateableContent: RateableContent, vote: IVote) {
        val response = httpClient(
            Request(Method.POST, "/notifications")
                .header("content-type", "application/json")
                .body(toJson(
                    createNotificationRequest(rateableContent, vote.createdAt(), vote.creator().userName, NotificationType.RATING)
                ))
        )
    }

    override fun notifyContentStatusChange(content: UserAddedContent) {
        val response = httpClient(
            Request(Method.POST, "/notifications")
                .header("content-type", "application/json")
                .body(toJson(
                    createNotificationRequest(content, content.statusUpdatedAt(), getAdministrationUser().userName, NotificationType.CONTENT_STATUS_CHANGE)
                ))
        )
    }

    override fun getAdministrationUser(): User {
        val client = RequireOkResponse().then(httpClient)
        val response = client(Request(Method.GET, "/get_administration_user"))
        return userFromResponse(fromJson(response.bodyString()))
    }

    override fun notifyAdministrationAboutReport(reportableContent: ReportableContent) {}

    override fun notifyContentWasCommented(commentableContent: CommentableContent, comment: IComment) {
        val response = httpClient(
            Request(Method.POST, "/notifications")
                .header("content-type", "application/json")
                .body(toJson(
                    createNotificationRequest(commentableContent, comment.createdAt(), comment.creator().userName, NotificationType.COMMENT)
                ))
        )
    }

    private fun userFromResponse(userResponse: GetUserResponse) = User(userResponse._id, userResponse.name, userResponse.surname, 1, userResponse.privilege == 1, userResponse.username)

    private fun formRedirectUrl(content: UserAddedContent): String {
        val routeCode = if (content is IPin) {
            content.routeCode()
        } else if (content is DiscussionElement) {
            content.routeCode()
        } else {
            ""
        }
        return "/?route=${routeCode}&${content.contentType().toString().toLowerCase()}=${content.id()}"

    }

    private fun createNotificationRequest(content: UserAddedContent, createdAt: LocalDateTime, senderName: String, notificationType: NotificationType): CreateNotificationRequest {
        return CreateNotificationRequest(
            notificationType,
            senderName,
            content.creator().userName,
            formRedirectUrl(content),
            createdAt.toLocalDate().toString(),
            content.contentType()
        )
    }
}

