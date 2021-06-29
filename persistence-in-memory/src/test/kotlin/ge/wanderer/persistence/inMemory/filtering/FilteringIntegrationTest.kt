package ge.wanderer.persistence.inMemory.filtering

import ge.wanderer.common.constants.TRANSIENT_ID
import ge.wanderer.common.dateTime
import ge.wanderer.common.listing.FilterOperation.*
import ge.wanderer.common.listing.FilterParam
import ge.wanderer.common.listing.ListingParams
import ge.wanderer.common.now
import ge.wanderer.core.integration.user.UserService
import ge.wanderer.core.model.content.RateableContent
import ge.wanderer.core.model.content.status.Active
import ge.wanderer.core.model.rating.Vote
import ge.wanderer.common.enums.VoteType
import ge.wanderer.persistence.inMemory.WandererInMemoryPersistenceApplication
import ge.wanderer.persistence.inMemory.support.createNewPostWithoutFiles
import ge.wanderer.persistence.repository.PinRepository
import ge.wanderer.persistence.repository.PostRepository
import org.joda.time.LocalDateTime
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.lang.IllegalStateException
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@SpringBootTest(classes = [WandererInMemoryPersistenceApplication::class])
class FilteringIntegrationTest(
    @Autowired private val pinRepository: PinRepository,
    @Autowired private val postRepository: PostRepository,
    @Autowired private val userService: UserService
) {

    @Test
    fun filtersByCreatorsId() {
        val pinsForUser1 = pinRepository.list(
            withFilters(
                FilterParam("creatorId", IS, "1")
            )
        )
        assertTrue(pinsForUser1.all { it.creator().id == "1" })

        val pinsForUser2 = pinRepository.list(
            withFilters(
                FilterParam("creatorId", IS, "2")
            )
        )
        assertTrue(pinsForUser2.all { it.creator().id == "2" })

        val pinsNotForUser1 = pinRepository.list(
            withFilters(
                FilterParam("creatorId", IS_NOT, "1")
            )
        )
        assertTrue(pinsNotForUser1.none { it.creator().id == "1" })

        val forRouteAndUser1 = pinRepository.listForRoute("123",
            withFilters(
                FilterParam("creatorId", IS, "1")
            )
        )
        assertTrue(forRouteAndUser1.all { it.creator().id == "1" && it.routeCode() == "123" && it.isActive() })

    }

    @Test
    fun filtersPostsByRating() {
        val post1 = postRepository.findById(1)
        val post2 = postRepository.findById(2)
        val post3 = postRepository.findById(3)

        // post 1 has 3 votes
        vote(post1, "5")
        vote(post1, "6")
        vote(post1, "7")

        // post 2 has 2 votes
        vote(post2, "5")
        vote(post2, "6")

        // post 3 has 1 vote
        vote(post3, "5")

        val postsWithRating3 = postRepository.list(
            withFilters(FilterParam("rating", IS, "3"))
        )
        assertTrue(postsWithRating3.all { it.rating() == 3 })
        assertTrue(postsWithRating3.contains(post1))

        val postsWithRating2 = postRepository.list(
            withFilters(FilterParam("rating", IS, "2"))
        )
        assertTrue(postsWithRating2.all { it.rating() == 2 })
        assertTrue(postsWithRating2.contains(post2))

        val postsWithRating1 = postRepository.list(
            withFilters(FilterParam("rating", IS, "1"))
        )
        assertTrue(postsWithRating1.all { it.rating() == 1 })
        assertTrue(postsWithRating1.contains(post3))

        val postsWithRating0 = postRepository.list(
            withFilters(FilterParam("rating", IS, "0"))
        )
        assertTrue(postsWithRating0.all { it.rating() == 0 })
        assertFalse(postsWithRating0.contains(post1))
        assertFalse(postsWithRating0.contains(post2))
        assertFalse(postsWithRating0.contains(post3))

        val ratingMoreThen1 = postRepository.list(
            withFilters(FilterParam("rating", IS_MORE_THEN, "1"))
        )
        assertTrue(ratingMoreThen1.all { it.rating() > 1 })
        assertTrue(ratingMoreThen1.contains(post1))
        assertTrue(ratingMoreThen1.contains(post2))
        assertFalse(ratingMoreThen1.contains(post3))
    }

    @Test
    fun filtersPostsByDate() {
        val post1 = postRepository.persist(postWithDate(dateTime("2021-04-18T12:10:08")))
        val post2 = postRepository.persist(postWithDate(dateTime("2021-04-18T12:10:09")))
        val post3 = postRepository.persist(postWithDate(dateTime("2021-04-18T12:10:10")))

        val filteredAfterDate = postRepository.list(
            withFilters(FilterParam("createdAt", IS_MORE_THEN, "2021-04-18T12:10:08"))
        )
        assertTrue(filteredAfterDate.contains(post2))
        assertTrue(filteredAfterDate.contains(post3))
        assertFalse(filteredAfterDate.contains(post1))
    }

    @Test
    fun failsWhenGivenTooManyFilterParams() {
        val exception = assertThrows<IllegalStateException> {
            postRepository.list(
                withFilters(
                    FilterParam("createdAt", IS_MORE_THEN, "2021-03-18T12:10:08"),
                    FilterParam("rating", IS_MORE_THEN, "10"),
                    FilterParam("isActive", IS, "true"),
                    FilterParam("jandaba", IS, "true")
                )
            )
        }

        assertEquals("Listing request can't have more then 3 filters!", exception.message!!)
    }

    private fun postWithDate(localDateTime: LocalDateTime) = createNewPostWithoutFiles(TRANSIENT_ID, userService.findUserById("1"), "aaa", localDateTime)

    private fun vote(content: RateableContent, voterId: String) {
        val user = userService.findUserById(voterId)
        content.giveVote(Vote(TRANSIENT_ID, user, now(), Active(now(), user), 1, VoteType.UP))
    }

    private fun withFilters(vararg filters: FilterParam) =
        ListingParams(100, 1, null, filters.asList())
}