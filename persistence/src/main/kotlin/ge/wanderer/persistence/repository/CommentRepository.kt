package ge.wanderer.persistence.repository

import ge.wanderer.core.model.comment.IComment
import ge.wanderer.core.model.content.CommentableContent
import ge.wanderer.common.listing.ListingParams

interface CommentRepository: BaseRepository<IComment> {
    fun listActiveFor(content: CommentableContent, listingParams: ListingParams): List<IComment>
}