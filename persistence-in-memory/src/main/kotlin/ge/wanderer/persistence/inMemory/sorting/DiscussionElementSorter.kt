package ge.wanderer.persistence.inMemory.sorting

import ge.wanderer.common.listing.SortingParams
import ge.wanderer.core.model.discussion.DiscussionElement
import org.springframework.stereotype.Component

@Component
class DiscussionElementSorter: SequenceSorter<DiscussionElement> {
    override fun sort(sequence: Sequence<DiscussionElement>, sortingParams: SortingParams): Sequence<DiscussionElement> =
        when(sortingParams.fieldName) {
            "createdAt" -> sequence.sortByCreateTime(sortingParams.sortingDirection)
            else -> error("Cant sort by ${sortingParams.fieldName}")
        }
}