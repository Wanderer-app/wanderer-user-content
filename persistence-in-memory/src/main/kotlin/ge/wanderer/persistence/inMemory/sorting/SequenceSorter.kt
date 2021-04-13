package ge.wanderer.persistence.inMemory.sorting

import ge.wanderer.common.listing.SortingParams

interface SequenceSorter<T> {
    fun sort(sequence: Sequence<T>, sortingParams: SortingParams): Sequence<T>
}