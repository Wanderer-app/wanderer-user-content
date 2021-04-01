package ge.wanderer.persistence.inMemory.repository

import ge.wanderer.common.listing.ListingParams
import ge.wanderer.core.repository.BaseRepository

abstract class BaseInMemoryRepository<T>: BaseRepository<T> {

    override fun findById(id: Long): T = data()[id] ?: error("Not found")
    override fun list(listingParams: ListingParams): List<T> = data().values.toList()

    override fun persist(model: T): T {
        data()[nextId()] = model
        return model
    }

    override fun delete(id: Long) {
        data().remove(id)
    }

    abstract fun data(): HashMap<Long, T>
    abstract fun nextId(): Long
}