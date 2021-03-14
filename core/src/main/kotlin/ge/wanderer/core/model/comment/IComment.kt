package ge.wanderer.core.model.comment

import ge.wanderer.core.model.content.Commentable
import ge.wanderer.core.model.content.UserAddedContent
import ge.wanderer.core.model.content.Rateable

interface IComment: UserAddedContent, Rateable, Commentable {
    fun text(): String
}