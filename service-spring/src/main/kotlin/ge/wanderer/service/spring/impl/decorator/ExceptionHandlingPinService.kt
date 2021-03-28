package ge.wanderer.service.spring.impl.decorator

import ge.wanderer.common.listing.ListingParams
import ge.wanderer.core.model.report.Report
import ge.wanderer.service.protocol.data.CommentData
import ge.wanderer.service.protocol.data.PinData
import ge.wanderer.service.protocol.data.PinMapData
import ge.wanderer.service.protocol.data.RatingData
import ge.wanderer.service.protocol.interfaces.PinService
import ge.wanderer.service.protocol.request.*
import ge.wanderer.service.protocol.response.ServiceListingResponse
import ge.wanderer.service.protocol.response.ServiceResponse
import ge.wanderer.service.spring.impl.PinServiceImpl
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Primary
import org.springframework.stereotype.Component

@Component
@Primary
class ExceptionHandlingPinService(
    @Autowired private val pinServiceImpl: PinServiceImpl
): PinService {
    override fun createPin(request: CreatePinRequest): ServiceResponse<PinData> = handle { pinServiceImpl.createPin(request) }

    override fun listForRoute(routeCode: String, listingParams: ListingParams): ServiceListingResponse<PinMapData> =
        handleListing { pinServiceImpl.listForRoute(routeCode, listingParams) }

    override fun list(listingParams: ListingParams): ServiceListingResponse<PinData> =
        handleListing { pinServiceImpl.list(listingParams) }

    override fun reportIrrelevant(request: OperateOnContentRequest): ServiceResponse<PinData> =
        handle { pinServiceImpl.reportIrrelevant(request) }

    override fun updatePin(request: UpdatePinRequest): ServiceResponse<PinData> =
        handle { pinServiceImpl.updatePin(request) }

    override fun findById(id: Long): ServiceResponse<PinData> =
        handle { pinServiceImpl.findById(id) }

    override fun activate(request: OperateOnContentRequest): ServiceResponse<PinData> =
        handle { pinServiceImpl.activate(request) }

    override fun remove(request: OperateOnContentRequest): ServiceResponse<PinData> =
        handle { pinServiceImpl.remove(request) }

    override fun giveUpVote(request: OperateOnContentRequest): ServiceResponse<RatingData> =
        handle { pinServiceImpl.giveUpVote(request) }

    override fun giveDownVote(request: OperateOnContentRequest): ServiceResponse<RatingData> =
        handle { pinServiceImpl.giveDownVote(request) }

    override fun removeVote(request: OperateOnContentRequest): ServiceResponse<RatingData> =
        handle { pinServiceImpl.removeVote(request) }

    override fun addComment(request: AddCommentRequest): ServiceResponse<CommentData> =
        handle { pinServiceImpl.addComment(request) }

    override fun listComments(contentId: Long, listingParams: ListingParams): ServiceListingResponse<CommentData> =
        handleListing { pinServiceImpl.listComments(contentId, listingParams) }

    override fun report(request: ReportContentRequest): ServiceResponse<Report> =
        handle { pinServiceImpl.report(request) }

    override fun listReportsForContent(contentId: Long): ServiceListingResponse<Report> =
        handleListing { pinServiceImpl.listReportsForContent(contentId) }
}