package ge.wanderer.persistence.inMemory.repository

import ge.wanderer.core.model.discussion.DiscussionElement
import ge.wanderer.common.listing.ListingParams
import ge.wanderer.common.listing.SortingParams
import ge.wanderer.persistence.inMemory.sorting.SequenceSorter
import ge.wanderer.persistence.repository.DiscussionRepository
import ge.wanderer.persistence.repository.PollRepository
import ge.wanderer.persistence.repository.PostRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class DiscussionRepositoryImpl(
    @Autowired private val postRepository: PostRepository,
    @Autowired private val pollRepository: PollRepository,
    @Autowired private val sorter: SequenceSorter<DiscussionElement>
): DiscussionRepository {
    override fun listForRoute(routeCode: String, listingParams: ListingParams): List<DiscussionElement> {
        val polls = pollRepository.list(listingParams.withoutSorting()).filter { it.routeCode() == routeCode }
        val posts = postRepository.list(listingParams.withoutSorting()).filter { it.routeCode() == routeCode }

        return listOf(polls, posts)
            .flatten()
            .asSequence()
            .sort(listingParams.sortingParams)
            .take(listingParams.batchSize)
            .toList()

    }

    private fun ListingParams.withoutSorting() = ListingParams(batchSize, batchNumber, null, filters)
    private fun Sequence<DiscussionElement>.sort(sortingParams: SortingParams?) =
        sortingParams ?.let { sorter.sort(this, sortingParams) } ?: let { this }
}