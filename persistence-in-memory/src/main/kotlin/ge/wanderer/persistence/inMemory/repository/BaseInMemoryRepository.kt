package ge.wanderer.persistence.inMemory.repository

import ge.wanderer.common.listing.ListingParams
import ge.wanderer.common.listing.SortingParams
import ge.wanderer.persistence.inMemory.sorting.SequenceSorter
import ge.wanderer.persistence.repository.BaseRepository

abstract class BaseInMemoryRepository<T>(
    private val sorter: SequenceSorter<T>
): BaseRepository<T> {

    override fun findById(id: Long): T = data()[id] ?: error("Not found")

    override fun list(listingParams: ListingParams): List<T> =
        data().values.applyListingParams(listingParams)

    override fun persist(model: T): T {
        val nextId = nextId()
        val persisted = makePersistent(model, nextId)
        data()[nextId] = persisted
        return persisted
    }

    override fun delete(id: Long) {
        data().remove(id)
    }

    protected fun Collection<T>.applyListingParams(params: ListingParams): List<T> =
        this
            .asSequence()
            .sortWith(params.sortingParams)
            .take(params.batchSize)
            .toList()

    private fun Sequence<T>.sortWith(params: SortingParams?): Sequence<T> =
        params?.let { sorter.sort(this, params) } ?:let { this }

    abstract fun data(): HashMap<Long, T>
    abstract fun nextId(): Long
    abstract fun makePersistent(model: T, id:Long): T
}