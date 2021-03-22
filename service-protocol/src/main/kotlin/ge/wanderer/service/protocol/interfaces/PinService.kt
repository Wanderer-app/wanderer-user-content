package ge.wanderer.service.protocol.interfaces

import ge.wanderer.service.protocol.data.PinData
import ge.wanderer.service.protocol.interfaces.base.CommentableContentService
import ge.wanderer.service.protocol.interfaces.base.RateableContentService
import ge.wanderer.service.protocol.interfaces.base.ReportableContentService
import ge.wanderer.service.protocol.interfaces.base.UserContentService
import ge.wanderer.service.protocol.request.CreatePinRequest
import ge.wanderer.service.protocol.request.ModifyContentRequest
import ge.wanderer.service.protocol.request.ReportContentRequest
import ge.wanderer.service.protocol.request.listing.ListingRequest
import ge.wanderer.service.protocol.response.ServiceListingResponse
import ge.wanderer.service.protocol.response.ServiceResponse

interface PinService : UserContentService<PinData>, RateableContentService, CommentableContentService, ReportableContentService {
    fun createPin(request: CreatePinRequest): ServiceResponse<PinData>
    fun listForRoute(routeCode: String, listingRequest: ListingRequest): ServiceListingResponse<PinData>
    fun reportIrrelevant(reportContentRequest: ReportContentRequest): ServiceResponse<PinData>
    fun markIrrelevant(modifyContentRequest: ModifyContentRequest): ServiceResponse<PinData>
}