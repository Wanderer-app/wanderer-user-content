package ge.wanderer.service.spring.impl.decorator

import ge.wanderer.core.model.report.Report
import ge.wanderer.persistence.listing.ListingParams
import ge.wanderer.service.protocol.data.CommentData
import ge.wanderer.service.protocol.data.RatingData
import ge.wanderer.service.protocol.interfaces.CommentService
import ge.wanderer.service.protocol.request.AddCommentRequest
import ge.wanderer.service.protocol.request.OperateOnContentRequest
import ge.wanderer.service.protocol.request.ReportContentRequest
import ge.wanderer.service.protocol.request.UpdateCommentRequest
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

    override fun findById(id: Long): ServiceResponse<CommentData> =
        handle { commentServiceImpl.findById(id) }

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

    override fun listComments(contentId: Long, listingParams: ListingParams): ServiceListingResponse<CommentData> =
        handleListing { commentServiceImpl.listComments(contentId, listingParams) }

    override fun report(request: ReportContentRequest): ServiceResponse<Report> =
        handle { commentServiceImpl.report(request) }

    override fun listReportsForContent(contentId: Long): ServiceListingResponse<Report> =
        handleListing { commentServiceImpl.listReportsForContent(contentId) }

}