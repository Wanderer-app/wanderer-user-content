package ge.wanderer.service.spring.impl.decorator

import ge.wanderer.common.listing.ListingParams
import ge.wanderer.service.protocol.data.*
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

    override fun list(listingParams: ListingParams, requestingUserId: Long?): ServiceListingResponse<PinData> =
        handleListing { pinServiceImpl.list(listingParams, requestingUserId) }

    override fun reportIrrelevant(request: OperateOnContentRequest): ServiceResponse<PinData> =
        handle { pinServiceImpl.reportIrrelevant(request) }

    override fun updatePin(request: UpdatePinRequest): ServiceResponse<PinData> =
        handle { pinServiceImpl.updatePin(request) }

    override fun findById(id: Long, requestingUserId: Long?): ServiceResponse<PinData> =
        handle { pinServiceImpl.findById(id, requestingUserId) }

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

    override fun listComments(request: ListCommentsRequest): ServiceListingResponse<CommentData> =
        handleListing { pinServiceImpl.listComments(request) }

    override fun report(request: ReportContentRequest): ServiceResponse<ReportData> =
        handle { pinServiceImpl.report(request) }

    override fun listReportsForContent(contentId: Long): ServiceListingResponse<ReportData> =
        handleListing { pinServiceImpl.listReportsForContent(contentId) }
}