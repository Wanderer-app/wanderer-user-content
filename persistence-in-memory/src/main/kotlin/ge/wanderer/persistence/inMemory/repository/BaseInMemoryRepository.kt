package ge.wanderer.persistence.inMemory.repository

import ge.wanderer.common.listing.FilterParam
import ge.wanderer.common.listing.ListingParams
import ge.wanderer.common.listing.SortingParams
import ge.wanderer.persistence.inMemory.filtering.FilterParameterEvaluator
import ge.wanderer.persistence.inMemory.pagination.paginate
import ge.wanderer.persistence.inMemory.sorting.SequenceSorter
import ge.wanderer.persistence.repository.BaseRepository

abstract class BaseInMemoryRepository<T>(
    private val sorter: SequenceSorter<T>,
    private val maxNumberOfFilters: Int = 3
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

    private fun Collection<T>.applyListingParams(params: ListingParams): List<T> {
        return this
            .asSequence()
            .applyFilters(params.filters)
            .sortWith(params.sortingParams)
            .toList()
            .paginate(params)
    }


    protected fun Sequence<T>.applyListingParams(params: ListingParams): List<T> =
        this
            .applyFilters(params.filters)
            .sortWith(params.sortingParams)
            .take(params.batchSize)
            .toList()

    private fun Sequence<T>.sortWith(params: SortingParams?): Sequence<T> =
        params?.let { sorter.sort(this, params) } ?:let { this }

    private fun Sequence<T>.applyFilters(filters: List<FilterParam>): Sequence<T> {
        check(filters.size <= maxNumberOfFilters) {"Listing request can't have more then $maxNumberOfFilters filters!"}

        var filtered = this
        filters.forEach { filterParam ->
            filtered = filtered.filter { element -> FilterParameterEvaluator(filterParam, element).evaluate() }
        }
        return filtered
    }


    abstract fun data(): HashMap<Long, T>
    abstract fun nextId(): Long
    abstract fun makePersistent(model: T, id:Long): T
}