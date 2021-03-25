package ge.wanderer.service.spring.impl

import ge.wanderer.common.listing.ListingParams
import ge.wanderer.core.repository.DiscussionRepository
import ge.wanderer.service.protocol.data.DiscussionElementData
import ge.wanderer.service.protocol.interfaces.DiscussionService
import ge.wanderer.service.protocol.response.ServiceListingResponse
import ge.wanderer.service.spring.CommentPreviewProvider
import ge.wanderer.service.spring.data.data
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class DiscussionServiceImpl(
    @Autowired private val discussionRepository: DiscussionRepository,
    @Autowired private val commentPreviewProvider: CommentPreviewProvider
): DiscussionService {

    override fun getDiscussionForRoute(routeCode: String, listingParams: ListingParams): ServiceListingResponse<DiscussionElementData> {
        val discussions = discussionRepository.listForRoute(routeCode, listingParams)
        return ServiceListingResponse(
            true,
            "Discussions fetched",
            discussions.size,
            listingParams.batchNumber,
            discussions.map { it.data(commentPreviewProvider.getPreviewFor(it)) })
    }

}