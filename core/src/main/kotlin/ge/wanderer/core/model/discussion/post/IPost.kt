package ge.wanderer.core.model.discussion.post

import ge.wanderer.core.model.content.Commentable
import ge.wanderer.core.model.content.Rateable
import ge.wanderer.core.model.discussion.DiscussionElement

interface IPost: DiscussionElement, Rateable, Commentable {
}