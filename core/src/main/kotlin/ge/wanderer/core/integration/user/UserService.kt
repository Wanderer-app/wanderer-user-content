package ge.wanderer.core.integration.user

import ge.wanderer.core.model.comment.IComment
import ge.wanderer.core.model.content.CommentableContent
import ge.wanderer.core.model.content.RateableContent
import ge.wanderer.core.model.content.ReportableContent
import ge.wanderer.core.model.content.UserAddedContent
import ge.wanderer.core.model.rating.IVote

interface UserService {
    fun findUserById(userId: String): User
    fun usersContentWasRated(rateableContent: RateableContent, vote: IVote)
    fun notifyContentStatusChange(content: UserAddedContent)
    fun getAdministrationUser(): User
    fun notifyAdministrationAboutReport(reportableContent: ReportableContent)
    fun notifyContentWasCommented(commentableContent: CommentableContent, comment: IComment)
}