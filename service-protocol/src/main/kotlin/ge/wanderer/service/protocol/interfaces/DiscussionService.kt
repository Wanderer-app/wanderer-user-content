package ge.wanderer.service.protocol.interfaces

import ge.wanderer.persistence.listing.ListingParams
import ge.wanderer.service.protocol.data.DiscussionElementData
import ge.wanderer.service.protocol.response.ServiceListingResponse

interface DiscussionService {

    fun getDiscussionForRoute(routeCode: String, listingParams: ListingParams): ServiceListingResponse<DiscussionElementData>
}