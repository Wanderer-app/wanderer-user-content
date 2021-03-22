package ge.wanderer.service.protocol.interfaces.base

import ge.wanderer.service.protocol.data.CommentData
import ge.wanderer.service.protocol.response.ServiceListingResponse
import ge.wanderer.service.protocol.response.ServiceResponse

interface CommentableContentService {
    fun addComment(): ServiceResponse<CommentData>
    fun listCommentsForContent(): ServiceListingResponse<CommentData>
}