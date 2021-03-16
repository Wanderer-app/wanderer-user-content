package ge.wanderer.core.model.discussion.poll

import ge.wanderer.core.model.content.CommentableContent
import ge.wanderer.core.model.discussion.DiscussionElement

interface IPoll: DiscussionElement, CommentableContent {
    fun addAnswer(answer: IPollAnswer)
    fun answersData(): Set<PollAnswerData>
}