package ge.wanderer.service.protocol.interfaces.base

import ge.wanderer.service.protocol.data.CommentData
import ge.wanderer.service.protocol.request.AddCommentRequest
import ge.wanderer.service.protocol.request.ListCommentsRequest
import ge.wanderer.service.protocol.response.ServiceListingResponse
import ge.wanderer.service.protocol.response.ServiceResponse

interface CommentableContentService {
    fun addComment(request: AddCommentRequest): ServiceResponse<CommentData>
    fun listComments(request: ListCommentsRequest): ServiceListingResponse<CommentData>
}