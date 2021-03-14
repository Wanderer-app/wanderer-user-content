package ge.wanderer.core.model.discussion.poll

data class PollContent(
    val question: String,
    val answers: Set<PollAnswerData>
)
