package ge.wanderer.service.spring.impl

import ge.wanderer.common.listing.ListingParams
import ge.wanderer.core.model.report.Report
import ge.wanderer.core.repository.PinRepository
import ge.wanderer.service.protocol.data.CommentData
import ge.wanderer.service.protocol.data.PinData
import ge.wanderer.service.protocol.data.RatingData
import ge.wanderer.service.protocol.interfaces.PinService
import ge.wanderer.service.protocol.request.*
import ge.wanderer.service.protocol.response.ServiceListingResponse
import ge.wanderer.service.protocol.response.ServiceResponse
import ge.wanderer.service.spring.CommentPreviewProvider
import ge.wanderer.service.spring.command.CommandProvider
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class PinServiceImpl(
    @Autowired private val pinRepository: PinRepository,
    @Autowired private val commentPreviewProvider: CommentPreviewProvider,
    @Autowired private val commandProvider: CommandProvider
): PinService {

    override fun createPin(request: CreatePinRequest): ServiceResponse<PinData> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun listForRoute(routeCode: String, listingParams: ListingParams): ServiceListingResponse<PinData> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun list(listingParams: ListingParams): ServiceListingResponse<PinData> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun reportIrrelevant(request: OperateOnContentRequest): ServiceResponse<PinData> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun updatePin(request: UpdatePinRequest): ServiceResponse<PinData> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun findById(id: Long): ServiceResponse<PinData> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun activate(request: OperateOnContentRequest): ServiceResponse<PinData> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun remove(request: OperateOnContentRequest): ServiceResponse<PinData> {
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

    override fun listComments(contentId: Long, listingParams: ListingParams): ServiceListingResponse<CommentData> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun report(request: ReportContentRequest): ServiceResponse<Report> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun listReportsForContent(contentId: Long): ServiceListingResponse<Report> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}