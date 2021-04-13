package ge.wanderer.persistence.inMemory.sorting

import ge.wanderer.common.listing.SortingParams
import ge.wanderer.core.model.discussion.post.IPost
import org.springframework.stereotype.Component

@Component
class PostSorter: SequenceSorter<IPost> {
    override fun sort(sequence: Sequence<IPost>, sortingParams: SortingParams): Sequence<IPost> =
        when(sortingParams.fieldName) {
            "rating" -> sequence.sortByRating(sortingParams.sortingDirection)
            "createdAt" -> sequence.sortByCreateTime(sortingParams.sortingDirection)
            else -> error("Cant sort by ${sortingParams.fieldName}")
        }

}