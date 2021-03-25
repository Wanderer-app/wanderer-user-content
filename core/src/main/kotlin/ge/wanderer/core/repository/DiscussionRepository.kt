package ge.wanderer.core.repository

import ge.wanderer.common.listing.ListingParams
import ge.wanderer.core.model.discussion.DiscussionElement

interface DiscussionRepository {

    fun listForRoute(routeCode: String, listingParams: ListingParams): List<DiscussionElement>
}