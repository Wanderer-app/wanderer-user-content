package ge.wanderer.persistence.repository

import ge.wanderer.core.model.map.IPin
import ge.wanderer.common.listing.ListingParams

interface PinRepository: BaseRepository<IPin> {

    fun listForRoute(routeCode: String, listingParams: ListingParams): List<IPin>
}