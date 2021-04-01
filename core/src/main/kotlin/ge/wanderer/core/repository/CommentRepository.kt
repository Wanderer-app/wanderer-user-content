package ge.wanderer.core.repository

import ge.wanderer.common.listing.ListingParams
import ge.wanderer.core.model.comment.IComment
import ge.wanderer.core.model.content.CommentableContent
import ge.wanderer.core.model.content.UserAddedContent

interface CommentRepository: BaseRepository<IComment> {
    fun listActiveFor(content: CommentableContent, listingParams: ListingParams): List<IComment>
}