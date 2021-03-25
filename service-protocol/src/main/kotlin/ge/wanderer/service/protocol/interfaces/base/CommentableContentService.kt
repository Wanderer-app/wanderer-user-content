package ge.wanderer.service.protocol.interfaces.base

import ge.wanderer.service.protocol.data.CommentData
import ge.wanderer.service.protocol.request.AddCommentRequest
import ge.wanderer.common.listing.ListingParams
import ge.wanderer.service.protocol.response.ServiceListingResponse
import ge.wanderer.service.protocol.response.ServiceResponse

interface CommentableContentService {
    fun addComment(request: AddCommentRequest): ServiceResponse<CommentData>
    fun listComments(contentId: Long, listingParams: ListingParams): ServiceListingResponse<CommentData>
}