package ge.wanderer.service.spring.integration.user.service

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
class UserServiceMockImpl: UserService {

    override fun findUserById(userId: String): User =
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

    private val mainAdmin = User("5760b116-6aab-4f04-b8be-650e27a85d09", "Nika", "Jamburia", 5, true)
    private val users = listOf(
        mainAdmin,
        User("85fa0681-b7bd-4ee3-b5b5-eb2672181ae2", "Nikoloz", "Patatishvili", 10, true),
        User("04e51444-85af-4d92-b89a-c8f761b7f3ea", "Nika", "Jangulashvili", 5, true),
        User("b41c2dd8-db85-4d96-a1f4-92f90851f7f2", "Vipi", "Soxumski", 1, false),
        User("755520ef-f06a-49e2-af7e-a0f4c19b1aba", "Giji", "Temo", 1, false),
        User("5673a717-9083-4150-8b7e-c3fb25675e3a", "Ivane", "Bidzinashvili", 1, false),
        User("90d36b5f-e060-4f67-a4a2-c6d06ee76b04", "Laravel", "Pitonishvili", 1, false),
        User("c5905a33-9bf9-4d2d-973f-fa7475fd4223", "Bozi", "Marika", 1, false),
        User("de74bb1d-c2cc-41f7-b8a0-7eee85b1084a", "Robenzon", "Soxiani", 1, false),
        User("db9ac04e-b985-4d84-8bd4-9ce26d1f4fae", "Irakli", "Oqroyaneli", 5, false)
    )

}