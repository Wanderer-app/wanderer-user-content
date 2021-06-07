package ge.wanderer.service.spring.impl.decorator

import ge.wanderer.common.listing.ListingParams
import ge.wanderer.service.protocol.data.DiscussionElementData
import ge.wanderer.service.protocol.interfaces.DiscussionService
import ge.wanderer.service.protocol.response.ServiceListingResponse
import ge.wanderer.service.spring.impl.DiscussionServiceImpl
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Primary
import org.springframework.stereotype.Component

@Component
@Primary
class ExceptionHandlingDiscussionService(
    @Autowired private val discussionServiceImpl: DiscussionServiceImpl
): DiscussionService {

    override fun getDiscussionForRoute(routeCode: String, requestingUserId: Long?, listingParams: ListingParams): ServiceListingResponse<DiscussionElementData> =
        handleListing { discussionServiceImpl.getDiscussionForRoute(routeCode, requestingUserId, listingParams) }
}