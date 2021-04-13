package ge.wanderer.persistence.inMemory.sorting

import ge.wanderer.common.listing.SortingParams
import ge.wanderer.core.model.comment.IComment
import org.springframework.stereotype.Component

@Component
class CommentSorter: SequenceSorter<IComment> {
    override fun sort(sequence: Sequence<IComment>, sortingParams: SortingParams): Sequence<IComment> =
        when(sortingParams.fieldName) {
            "rating" -> sequence.sortByRating(sortingParams.sortingDirection)
            "createdAt" -> sequence.sortByCreateTime(sortingParams.sortingDirection)
            else -> error("Cant sort by ${sortingParams.fieldName}")
        }
}