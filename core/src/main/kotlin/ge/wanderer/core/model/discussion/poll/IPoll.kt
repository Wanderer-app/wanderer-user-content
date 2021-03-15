package ge.wanderer.core.model.discussion.poll

import ge.wanderer.core.model.content.Commentable
import ge.wanderer.core.model.discussion.DiscussionElement

interface IPoll: DiscussionElement, Commentable {
    fun addAnswer(answer: IPollAnswer)
    fun answersData(): Set<PollAnswerData>
}