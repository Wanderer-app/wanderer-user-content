package ge.wanderer.core.model.discussion.poll

import java.math.BigDecimal

data class PollAnswerData (
    val answerId: Long,
    val title: String,
    val answererIds: List<String>,
    val percentage: BigDecimal
)