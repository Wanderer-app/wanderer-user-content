package ge.wanderer.integration_tests.spring_inMemory

import ge.wanderer.persistence.listing.ListingParams
import java.math.BigDecimal

val DEFAULT_LISTING_PARAMS = ListingParams(30, 1, null, listOf())

data class PollData(
    val question: String,
    val answers: Set<AnswerInfo>
)

data class AnswerInfo (
    val answerId: Long,
    val title: String,
    val answererIds: List<Long>,
    val percentage: BigDecimal
)