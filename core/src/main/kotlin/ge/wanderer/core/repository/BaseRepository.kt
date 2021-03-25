package ge.wanderer.core.repository

import ge.wanderer.common.listing.ListingParams

interface BaseRepository<T> {
    fun findById(id: Long): T
    fun list(listingParams: ListingParams): List<T>
    fun persist(model: T): T
    fun delete(model: T)
}