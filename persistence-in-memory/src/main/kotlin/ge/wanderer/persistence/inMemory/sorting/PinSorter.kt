package ge.wanderer.persistence.inMemory.sorting

import ge.wanderer.common.listing.SortingParams
import ge.wanderer.core.model.map.IPin
import org.springframework.stereotype.Component

@Component
class PinSorter: SequenceSorter<IPin> {
    override fun sort(sequence: Sequence<IPin>, sortingParams: SortingParams): Sequence<IPin> =
        when(sortingParams.fieldName) {
            "rating" -> sequence.sortByRating(sortingParams.sortingDirection)
            "createdAt" -> sequence.sortByCreateTime(sortingParams.sortingDirection)
            else -> error("Cant sort by ${sortingParams.fieldName}")
        }

}