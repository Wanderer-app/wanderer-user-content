package ge.wanderer.service.protocol.interfaces

import ge.wanderer.service.protocol.data.CommentData
import ge.wanderer.service.protocol.interfaces.base.CommentableContentService
import ge.wanderer.service.protocol.interfaces.base.RateableContentService
import ge.wanderer.service.protocol.interfaces.base.ReportableContentService
import ge.wanderer.service.protocol.interfaces.base.UserContentService
import ge.wanderer.service.protocol.request.UpdateCommentRequest
import ge.wanderer.service.protocol.response.ServiceResponse

interface CommentService: UserContentService<CommentData>, RateableContentService, CommentableContentService, ReportableContentService {

    fun updateComment(request: UpdateCommentRequest): ServiceResponse<CommentData>
}