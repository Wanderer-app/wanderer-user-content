package ge.wanderer.core.repository

import ge.wanderer.common.listing.ListingParams
import ge.wanderer.core.model.map.IPin

interface PinRepository: BaseRepository<IPin> {

    fun listForRoute(routeCode: String, listingParams: ListingParams): List<IPin>
}