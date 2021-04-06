package ge.wanderer.persistence.inMemory.repository

import ge.wanderer.core.model.discussion.DiscussionElement
import ge.wanderer.common.listing.ListingParams
import ge.wanderer.persistence.repository.DiscussionRepository
import ge.wanderer.persistence.repository.PollRepository
import ge.wanderer.persistence.repository.PostRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class DiscussionRepositoryImpl(
    @Autowired private val postRepository: PostRepository,
    @Autowired private val pollRepository: PollRepository
): DiscussionRepository {
    override fun listForRoute(routeCode: String, listingParams: ListingParams): List<DiscussionElement> {
        val polls = pollRepository.list(listingParams).filter { it.routeCode() == routeCode }
        val posts = postRepository.list(listingParams).filter { it.routeCode() == routeCode }

        return listOf(polls, posts).flatten().sortedByDescending { it.createdAt() }
    }
}