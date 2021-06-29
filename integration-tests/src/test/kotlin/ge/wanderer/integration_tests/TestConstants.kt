package ge.wanderer.integration_tests

import ge.wanderer.common.listing.ListingParams
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import java.math.BigDecimal

val DEFAULT_LISTING_PARAMS = ListingParams(100, 1, null, listOf())
const val DEFAULT_LOGGED_IN_USER_ID = "5760b116-6aab-4f04-b8be-650e27a85d09"

data class PollData(
    val question: String,
    val answers: Set<AnswerInfo>
)

data class AnswerInfo (
    val answerId: Long,
    val title: String,
    val answererIds: List<String>,
    val percentage: BigDecimal
)

fun MockMvc.post(url: String, jsonBody: String) =
    this.perform(
        MockMvcRequestBuilders.post(url)
            .header("user-token", DEFAULT_LOGGED_IN_USER_ID)
            .content(jsonBody)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
    )

fun MockMvc.get(url: String) =
    this.perform(MockMvcRequestBuilders.get(url)
        .header("user-token", DEFAULT_LOGGED_IN_USER_ID)
        .accept(MediaType.APPLICATION_JSON))
