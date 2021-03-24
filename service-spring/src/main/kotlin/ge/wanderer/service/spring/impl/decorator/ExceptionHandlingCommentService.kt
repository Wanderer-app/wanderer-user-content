package ge.wanderer.service.spring.impl.decorator

import ge.wanderer.common.listing.ListingRequest
import ge.wanderer.core.model.report.Report
import ge.wanderer.service.protocol.data.CommentData
import ge.wanderer.service.protocol.data.RatingData
import ge.wanderer.service.protocol.interfaces.CommentService
import ge.wanderer.service.protocol.request.AddCommentRequest
import ge.wanderer.service.protocol.request.OperateOnContentRequest
import ge.wanderer.service.protocol.request.ReportContentRequest
import ge.wanderer.service.protocol.request.UpdateCommentRequest
import ge.wanderer.service.protocol.response.NoDataResponse
import ge.wanderer.service.protocol.response.ServiceListingResponse
import ge.wanderer.service.protocol.response.ServiceResponse
import ge.wanderer.service.spring.impl.CommentServiceImpl
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Primary
import org.springframework.stereotype.Component
import java.lang.Exception

@Component
@Primary
class ExceptionHandlingCommentService(
    @Autowired private val commentServiceImpl: CommentServiceImpl
): CommentService {
    override fun updateComment(request: UpdateCommentRequest): ServiceResponse<CommentData> =
        try {
            commentServiceImpl.updateComment(request)
        } catch (e: Exception) {
            ServiceResponse(false, e.message!!, null)
        }
    

    override fun findById(id: Long): ServiceResponse<CommentData> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun activate(request: OperateOnContentRequest): ServiceResponse<CommentData> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun remove(request: OperateOnContentRequest): ServiceResponse<CommentData> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun giveUpVote(request: OperateOnContentRequest): ServiceResponse<RatingData> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun giveDownVote(request: OperateOnContentRequest): ServiceResponse<RatingData> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun removeVote(request: OperateOnContentRequest): ServiceResponse<RatingData> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun addComment(request: AddCommentRequest): ServiceResponse<CommentData> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun listComments(contentId: Long, listingRequest: ListingRequest): ServiceListingResponse<CommentData> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun report(request: ReportContentRequest): NoDataResponse {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun listReportsForContent(contentId: Long): ServiceListingResponse<Report> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}