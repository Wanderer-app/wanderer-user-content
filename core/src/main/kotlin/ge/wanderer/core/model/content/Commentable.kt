package ge.wanderer.core.model.content

import ge.wanderer.core.model.comment.Comment

interface Commentable {
    fun comments(): MutableList<Comment>
    fun addComment(comment: Comment)
}