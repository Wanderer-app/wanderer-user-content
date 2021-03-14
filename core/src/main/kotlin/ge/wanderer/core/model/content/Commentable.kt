package ge.wanderer.core.model.content

import ge.wanderer.core.model.comment.IComment

interface Commentable {
    fun comments(): MutableList<IComment>
    fun addComment(comment: IComment)
}