package ge.wanderer.core.model.content

import ge.wanderer.core.model.comment.IComment

interface CommentableContent: UserAddedContent {
    fun comments(): List<IComment>
    fun addComment(comment: IComment): IComment
}