package ge.wanderer.core.repository

import ge.wanderer.common.listing.ListingParams
import ge.wanderer.core.model.comment.IComment
import ge.wanderer.core.model.content.UserAddedContent

interface CommentRepository: BaseRepository<IComment> {
    fun listActiveFor(content: UserAddedContent, listingParams: ListingParams): List<IComment>
}