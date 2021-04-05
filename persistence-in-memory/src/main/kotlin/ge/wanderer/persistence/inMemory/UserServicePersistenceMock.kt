package ge.wanderer.persistence.inMemory

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
class UserServicePersistenceMock: UserService {

    override fun findUserById(userId: Long): User =
        try {
            users.first { it.id == userId }
        } catch (e: NoSuchElementException) {
            throw IllegalStateException("User with id $userId not found!")
        }
    override fun getAdministrationUser(): User = mainAdmin
    override fun usersContentWasRated(rateableContent: RateableContent, vote: IVote) { }
    override fun notifyContentStatusChange(content: UserAddedContent) { }
    override fun notifyAdministrationAboutReport(reportableContent: ReportableContent) { }
    override fun notifyContentWasCommented(commentableContent: CommentableContent, comment: IComment) { }

    private val mainAdmin = User(1, "Nika", "Jamburia", 5, true)
    private val users = listOf(
        mainAdmin,
        User(2, "Nikoloz", "Patatishvili", 10, true),
        User(3, "Nika", "Jangulashvili", 2, true),
        User(4, "Vipi", "Soxumski", 1, false),
        User(5, "Giji", "Temo", 1, false),
        User(6, "Ivane", "Bidzinashvili", 1, false),
        User(7, "Laravel", "Pitonishvili", 1, false),
        User(8, "Bozi", "Marika", 1, false),
        User(9, "Robenzon", "Soxiani", 1, false),
        User(10, "Irakli", "Oqroyaneli", 5, false)
    )
}