package ge.wanderer.integration_tests.web_spring_inMemory

import ge.wanderer.common.functions.amount
import ge.wanderer.common.constants.TRANSIENT_ID
import ge.wanderer.common.dateTime
import ge.wanderer.common.functions.fromJson
import ge.wanderer.common.functions.toJson
import ge.wanderer.common.now
import ge.wanderer.integration_tests.*
import ge.wanderer.service.protocol.data.CommentData
import ge.wanderer.service.protocol.data.DiscussionElementData
import ge.wanderer.service.protocol.request.*
import ge.wanderer.service.protocol.response.ServiceListingResponse
import ge.wanderer.service.protocol.response.ServiceResponse
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestPropertySource
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue

@SpringBootTest(classes = [SpringWebAppWithInMemoryPersistence::class], webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@TestPropertySource("classpath:service-spring-test.properties")
class PollControllerTest(
    @Autowired private val mockMvc: MockMvc
) {

    private val controllerPath = "/api/polls/"

    @Test
    fun createsAndUpdatesPoll() {
        val request = CreatePollRequest(now(), 1, "1234", "ragaca ragaca", setOf("a1", "a2", "a3"))

        val responseString = mockMvc.post(controllerPath + "create", toJson(request))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.isSuccessful").value(true))
            .andExpect(jsonPath("$.message").value("Poll created!. New model persisted successfully"))
            .andReturn()
            .response.contentAsString

        val poll = fromJson<ServiceResponse<DiscussionElementData>>(responseString).data!!
        assertNotEquals(TRANSIENT_ID, poll.id)
        assertTrue(poll.content.contains("a1"))
        assertTrue(poll.content.contains("a2"))
        assertTrue(poll.content.contains("a3"))

        val updateRequest = UpdatePollRequest(poll.id, "Some question", 1)
        val updateResponse = mockMvc.post(controllerPath + "update", toJson(updateRequest))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.isSuccessful").value(true))
            .andExpect(jsonPath("$.message").value("POLL updated"))
            .andReturn()
            .response.contentAsString

        val updatedPoll = fromJson<ServiceResponse<DiscussionElementData>>(updateResponse).data!!
        assertTrue(updatedPoll.content.contains("Some question"))
    }

    @Test
    fun addsAndRemovesAnswerToPoll() {
        val poll = createPoll(CreatePollRequest(now(), 1, "1234", "ragaca ragaca", setOf("a1", "a2", "a3")))

        var responseString = mockMvc.post(controllerPath + "add-answer", toJson(AddAnswerToPollRequest(poll.id, poll.creator.id, "a4", now())))
            .andExpect(status().isOk)
            .andReturn()
            .response.contentAsString

        var answers = fromJson<ServiceResponse<DiscussionElementData>>(responseString).pollAnswersInfo()
        assertEquals(4, answers.size)
        assertTrue(answers.any { it.title == "a2" })

        val answerToRemove = answers.first { it.title == "a2" }
        val request = RemovePollAnswerRequest(poll.id, answerToRemove.answerId, poll.creator.id, now())

        responseString = mockMvc.post(controllerPath + "remove-answer", toJson(request))
            .andExpect(status().isOk)
            .andReturn()
            .response.contentAsString
        answers = fromJson<ServiceResponse<DiscussionElementData>>(responseString).pollAnswersInfo()

        assertEquals(3, answers.size)
        assertTrue(answers.none { it.answerId == answerToRemove.answerId })
    }

    @Test
    fun selectsPollAnswer() {
        val poll = createPoll(CreatePollRequest(now(), 1, "1234", "ragaca ragaca", setOf("a1", "a2", "a3")))
        val answerToSelect = fromJson<PollData>(poll.content).answers.first { it.title == "a2" }

        val responseString = mockMvc.post(controllerPath + "select-answer", toJson(SelectPollAnswerRequest(poll.id, 2, answerToSelect.answerId)))
            .andExpect(status().isOk)
            .andReturn()
            .response.contentAsString

        val selectedAnswer = fromJson<ServiceResponse<DiscussionElementData>>(responseString)
            .pollAnswersInfo()
            .first { it.answerId == answerToSelect.answerId }

        assertEquals(1, selectedAnswer.answererIds.size)
        assertEquals(amount(100.00), selectedAnswer.percentage)
        assertTrue(selectedAnswer.answererIds.contains(2))
    }

    @Test
    fun findsPollById() {
        val poll = createPoll(CreatePollRequest(now(), 1, "1234", "ragaca ragaca", setOf("a1", "a2", "a3")))

        mockMvc.get(controllerPath + poll.id)
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.isSuccessful").value(true))
            .andExpect(jsonPath("$.message").value("Poll fetched"))
            .andExpect(jsonPath("$.data.id").value(poll.id))
    }

    @Test
    fun activatesAndRemovesPolls() {
        val poll = createPoll(CreatePollRequest(now(), 1, "1234", "ragaca ragaca", setOf("a1", "a2", "a3")))

        mockMvc.post(controllerPath + "remove", toJson(OperateOnContentRequest(poll.id, 2, now())))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.isSuccessful").value(true))
            .andExpect(jsonPath("$.message").value("POLL removed successfully!"))
            .andExpect(jsonPath("$.data.id").value(poll.id))
            .andExpect(jsonPath("$.data.isActive").value(false))
            .andExpect(jsonPath("$.data.isRemoved").value(true))

        mockMvc.post(controllerPath + "activate", toJson(OperateOnContentRequest(poll.id, 2, dateTime("2021-04-05T12:00:00"))))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.isSuccessful").value(true))
            .andExpect(jsonPath("$.message").value("POLL activated successfully!"))
            .andExpect(jsonPath("$.data.id").value(poll.id))
            .andExpect(jsonPath("$.data.isActive").value(true))
            .andExpect(jsonPath("$.data.isRemoved").value(false))
            .andExpect(jsonPath("$.data.updatedAt").value("2021-04-05T12:00:00.000"))
    }

    @Test
    fun commentsPoll() {
        val pollId = 1L

        val request = AddCommentRequest(pollId, 2, "Some comment", now())
        mockMvc.post(controllerPath + "add-comment", toJson(request))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.isSuccessful").value(true))
            .andExpect(jsonPath("$.message").value("Comment added"))
            .andExpect(jsonPath("$.data.text").value("Some comment"))
            .andExpect(jsonPath("$.data.author.id").value(2))

        val commentsResponseString = mockMvc.post(controllerPath + "${pollId}/comments", toJson(DEFAULT_LISTING_PARAMS))
            .andExpect(status().isOk)
            .andReturn().response.contentAsString

        val comments = fromJson<ServiceListingResponse<CommentData>>(commentsResponseString).data
        assertTrue { comments.any { it.text == "Some comment" && it.author.id == 2L && it.id != TRANSIENT_ID } }
    }


    private fun createPoll(request: CreatePollRequest): DiscussionElementData =
        fromJson<ServiceResponse<DiscussionElementData>>(
            mockMvc.post(controllerPath + "create", toJson(request)).andReturn().response.contentAsString
        ).data!!

    private fun ServiceResponse<DiscussionElementData>.pollAnswersInfo(): Set<AnswerInfo> =
        fromJson<PollData>(this.data!!.content).answers


}