package ge.wanderer.persistence.inMemory.repository

import ge.wanderer.common.listing.ListingParams
import ge.wanderer.persistence.repository.BaseRepository

abstract class BaseInMemoryRepository<T>: BaseRepository<T> {

    override fun findById(id: Long): T = data()[id] ?: error("Not found")
    override fun list(listingParams: ListingParams): List<T> = data().values.toList()

    override fun persist(model: T): T {
        val nextId = nextId()
        val persisted = makePersistent(model, nextId)
        data()[nextId] = persisted
        return persisted
    }

    override fun delete(id: Long) {
        data().remove(id)
    }

    abstract fun data(): HashMap<Long, T>
    abstract fun nextId(): Long
    abstract fun makePersistent(model: T, id:Long): T
}