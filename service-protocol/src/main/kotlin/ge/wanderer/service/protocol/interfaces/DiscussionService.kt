package ge.wanderer.service.protocol.interfaces

import ge.wanderer.service.protocol.data.DiscussionElementData
import ge.wanderer.service.protocol.interfaces.base.CommentableContentService
import ge.wanderer.service.protocol.interfaces.base.RateableContentService
import ge.wanderer.service.protocol.interfaces.base.ReportableContentService
import ge.wanderer.service.protocol.interfaces.base.UserContentService
import ge.wanderer.common.listing.ListingRequest
import ge.wanderer.service.protocol.response.ServiceListingResponse

interface DiscussionService : UserContentService<DiscussionElementData>, RateableContentService, CommentableContentService, ReportableContentService {

    fun getDiscussionForRoute(routeCode: String, listingRequest: ListingRequest): ServiceListingResponse<DiscussionElementData>
    fun list(listingRequest: ListingRequest): ServiceListingResponse<DiscussionElementData>

}