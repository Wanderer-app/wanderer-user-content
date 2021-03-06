package ge.wanderer.persistence.repository

import ge.wanderer.core.model.discussion.DiscussionElement
import ge.wanderer.common.listing.ListingParams

interface DiscussionRepository {

    fun listForRoute(routeCode: String, listingParams: ListingParams): List<DiscussionElement>
}