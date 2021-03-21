package ge.wanderer.core.model.discussion.poll

import ge.wanderer.core.integration.user.User
import ge.wanderer.core.model.content.CommentableContent
import ge.wanderer.core.model.discussion.DiscussionElement

interface IPoll: DiscussionElement, CommentableContent {
    fun addAnswer(answer: IPollAnswer)
    fun selectAnswer(answerId: Long, user: User)
    fun answersData(): Set<PollAnswerData>
}