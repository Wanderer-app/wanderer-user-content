package ge.wanderer.service.protocol.interfaces

import ge.wanderer.service.protocol.data.PinData
import ge.wanderer.service.protocol.interfaces.base.CommentableContentService
import ge.wanderer.service.protocol.interfaces.base.RateableContentService
import ge.wanderer.service.protocol.interfaces.base.ReportableContentService
import ge.wanderer.service.protocol.interfaces.base.UserContentService
import ge.wanderer.service.protocol.request.CreatePinRequest
import ge.wanderer.service.protocol.request.OperateOnContentRequest
import ge.wanderer.service.protocol.request.UpdatePinRequest
import ge.wanderer.common.listing.ListingParams
import ge.wanderer.service.protocol.response.ServiceListingResponse
import ge.wanderer.service.protocol.response.ServiceResponse

interface PinService : UserContentService<PinData>, RateableContentService, CommentableContentService, ReportableContentService {

    fun createPin(request: CreatePinRequest): ServiceResponse<PinData>
    fun listForRoute(routeCode: String, listingParams: ListingParams): ServiceListingResponse<PinData>
    fun list(listingParams: ListingParams): ServiceListingResponse<PinData>
    fun reportIrrelevant(request: OperateOnContentRequest): ServiceResponse<PinData>
    fun updatePin(request: UpdatePinRequest): ServiceResponse<PinData>
}