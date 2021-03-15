package ge.wanderer.core.model.discussion.poll

import ge.wanderer.core.data.user.User
import ge.wanderer.core.model.content.UserAddedContent

interface IPollAnswer: UserAddedContent {

    fun selectBy(user: User)
}