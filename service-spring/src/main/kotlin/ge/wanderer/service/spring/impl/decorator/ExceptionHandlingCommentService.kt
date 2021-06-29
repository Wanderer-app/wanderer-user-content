package ge.wanderer.service.spring.impl.decorator

import ge.wanderer.service.protocol.data.CommentData
import ge.wanderer.service.protocol.data.RatingData
import ge.wanderer.service.protocol.data.ReportData
import ge.wanderer.service.protocol.interfaces.CommentService
import ge.wanderer.service.protocol.request.*
import ge.wanderer.service.protocol.response.ServiceListingResponse
import ge.wanderer.service.protocol.response.ServiceResponse
import ge.wanderer.service.spring.impl.CommentServiceImpl
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Primary
import org.springframework.stereotype.Component

@Component
@Primary
class ExceptionHandlingCommentService(
    @Autowired private val commentServiceImpl: CommentServiceImpl
) : CommentService {

    override fun updateComment(request: UpdateCommentRequest): ServiceResponse<CommentData> =
        handle { commentServiceImpl.updateComment(request) }

    override fun findById(id: Long, requestingUserId: String?): ServiceResponse<CommentData> =
        handle { commentServiceImpl.findById(id, requestingUserId) }

    override fun activate(request: OperateOnContentRequest): ServiceResponse<CommentData> =
        handle { commentServiceImpl.activate(request) }

    override fun remove(request: OperateOnContentRequest): ServiceResponse<CommentData> =
        handle { commentServiceImpl.remove(request) }

    override fun giveUpVote(request: OperateOnContentRequest): ServiceResponse<RatingData> =
        handle { commentServiceImpl.giveUpVote(request) }

    override fun giveDownVote(request: OperateOnContentRequest): ServiceResponse<RatingData> =
        handle { commentServiceImpl.giveDownVote(request) }

    override fun removeVote(request: OperateOnContentRequest): ServiceResponse<RatingData> =
        handle { commentServiceImpl.removeVote(request) }

    override fun addComment(request: AddCommentRequest): ServiceResponse<CommentData> =
        handle { commentServiceImpl.addComment(request) }

    override fun listComments(request: ListCommentsRequest): ServiceListingResponse<CommentData> =
        handleListing { commentServiceImpl.listComments(request) }

    override fun report(request: ReportContentRequest): ServiceResponse<ReportData> =
        handle { commentServiceImpl.report(request) }

    override fun listReportsForContent(contentId: Long): ServiceListingResponse<ReportData> =
        handleListing { commentServiceImpl.listReportsForContent(contentId) }

}