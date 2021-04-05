package ge.wanderer.service.spring.integration.user

import ge.wanderer.core.integration.user.User
import ge.wanderer.core.integration.user.UserService
import ge.wanderer.core.model.comment.IComment
import ge.wanderer.core.model.content.CommentableContent
import ge.wanderer.core.model.content.RateableContent
import ge.wanderer.core.model.content.ReportableContent
import ge.wanderer.core.model.content.UserAddedContent
import ge.wanderer.core.model.rating.IVote
import org.springframework.stereotype.Component

@Component
class UserServiceImpl: UserService {

    override fun findUserById(userId: Long): User = error("User service not ready yet")
    override fun usersContentWasRated(rateableContent: RateableContent, vote: IVote) {}
    override fun notifyContentStatusChange(content: UserAddedContent) {}
    override fun getAdministrationUser(): User = error("User service not ready yet")
    override fun notifyAdministrationAboutReport(reportableContent: ReportableContent) {}
    override fun notifyContentWasCommented(commentableContent: CommentableContent, comment: IComment) {}
}