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
    override fun findUserById(userId: Long): User {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun usersContentWasRated(rateableContent: RateableContent, vote: IVote) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun notifyContentStatusChange(content: UserAddedContent) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getAdministrationUser(): User {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun notifyAdministrationAboutReport(reportableContent: ReportableContent) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun notifyContentWasCommented(commentableContent: CommentableContent, comment: IComment) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}