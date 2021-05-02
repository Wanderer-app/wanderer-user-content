package ge.wanderer.service.protocol.interfaces

import ge.wanderer.common.listing.ListingParams
import ge.wanderer.service.protocol.data.DiscussionElementData
import ge.wanderer.service.protocol.response.ServiceListingResponse

interface DiscussionService {

    fun getDiscussionForRoute(routeCode: String, userId: Long, listingParams: ListingParams): ServiceListingResponse<DiscussionElementData>
}