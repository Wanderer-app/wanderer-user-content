package ge.wanderer.core.model.discussion.poll

import ge.wanderer.core.integration.user.User
import ge.wanderer.core.model.content.UserAddedContent

interface IPollAnswer: UserAddedContent {
    fun selectBy(user: User)
    fun data(totalAnswerers: Int): PollAnswerData
    fun numberOfAnswerers(): Int
    fun selectors(): List<User>
    fun text(): String
}