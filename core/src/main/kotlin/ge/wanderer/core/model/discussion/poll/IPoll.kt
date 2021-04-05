package ge.wanderer.core.model.discussion.poll

import ge.wanderer.core.integration.user.User
import ge.wanderer.core.model.discussion.DiscussionElement

interface IPoll: DiscussionElement {
    fun addAnswer(answer: IPollAnswer)
    fun selectAnswer(answerId: Long, user: User)
    fun answersData(): Set<PollAnswerData>
    fun answers(): List<IPollAnswer>
}