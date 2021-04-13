package ge.wanderer.persistence.inMemory.sorting

import ge.wanderer.common.listing.SortingParams
import ge.wanderer.core.model.discussion.poll.IPoll
import org.springframework.stereotype.Component

@Component
class PollSorter: SequenceSorter<IPoll> {
    override fun sort(sequence: Sequence<IPoll>, sortingParams: SortingParams): Sequence<IPoll> =
        when(sortingParams.fieldName) {
            "createdAt" -> sequence.sortByCreateTime(sortingParams.sortingDirection)
            else -> error("Cant sort by ${sortingParams.fieldName}")
        }

}