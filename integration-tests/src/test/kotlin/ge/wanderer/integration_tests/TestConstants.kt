package ge.wanderer.integration_tests

import ge.wanderer.common.listing.ListingParams
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
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

fun postWithJson(url: String, content: String): MockHttpServletRequestBuilder =
    MockMvcRequestBuilders.post(url)
        .content(content)
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)