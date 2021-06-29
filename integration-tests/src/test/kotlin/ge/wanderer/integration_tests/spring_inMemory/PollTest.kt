package ge.wanderer.integration_tests.spring_inMemory

import ge.wanderer.common.*
import ge.wanderer.common.constants.TRANSIENT_ID
import ge.wanderer.common.functions.amount
import ge.wanderer.common.functions.fromJson
import ge.wanderer.common.functions.zeroAmount
import ge.wanderer.integration_tests.*
import ge.wanderer.service.protocol.data.DiscussionElementData
import ge.wanderer.service.protocol.interfaces.PollService
import ge.wanderer.service.protocol.request.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestPropertySource
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue

@SpringBootTest(classes = [SpringServiceWithInMemoryPersistenceApp::class])
@TestPropertySource("classpath:service-spring-test.properties")
class PollTest(
    @Autowired private val pollService: PollService
) {
    @Test
    fun findsById() {
        val response = pollService.findById(1, DEFAULT_LOGGED_IN_USER_ID)

        assertTrue(response.isSuccessful)
        assertEquals("Poll fetched", response.message)
        assertEquals(1, response.data!!.id)
    }

    @Test
    fun createsPoll() {
        val request = CreatePollRequest(now(), "5760b116-6aab-4f04-b8be-650e27a85d09", "123456", "What is the best video game?", setOf("Rdr", "Gta"))
        val response = pollService.createPoll(request)

        assertTrue(response.isSuccessful)
        assertEquals("Poll created!. New model persisted successfully", response.message)

        val data = response.data!!
        assertTrue(data.content.contains("What is the best video game?"))
        assertNotEquals(TRANSIENT_ID, data.id)

        val poll = pollService.findById(data.id, DEFAULT_LOGGED_IN_USER_ID).data!!
        assertTrue(poll.content.contains("What is the best video game?"))
        assertTrue(poll.content.contains("Rdr"))
        assertTrue(poll.content.contains("Gta"))
    }

    @Test
    fun addsAnswer() {
        val poll = pollService.createPoll(
            CreatePollRequest(now(), "5760b116-6aab-4f04-b8be-650e27a85d09", "123456", "What is the best video game?", setOf("Rdr", "Gta"))
        ).data!!

        val response = pollService.addAnswer(AddAnswerToPollRequest(poll.id, poll.creator.id, "Witcher", now()))
        assertTrue(response.isSuccessful)
        assertEquals("Answer added!", response.message)
        assertTrue(response.data!!.content.contains("Witcher"))
    }

    @Test
    fun removesAnswer() {
        val poll = pollService.createPoll(
            CreatePollRequest(now(), "5760b116-6aab-4f04-b8be-650e27a85d09", "123456", "What is the best video game?", setOf("Rdr", "Gta", "Witcher"))
        ).data!!
        val answerInfo = poll.answerInfo()
        val answerToRemove = answerInfo.first { it.title == "Witcher" }

        val response = pollService.removeAnswer(RemovePollAnswerRequest(poll.id, answerToRemove.answerId, poll.creator.id, now()))
        assertTrue(response.isSuccessful)
        assertEquals("Answer Removed!", response.message)
        assertFalse(response.data!!.content.contains("Witcher"))
        assertFalse(pollService.findById(poll.id, DEFAULT_LOGGED_IN_USER_ID).data!!.content.contains("Witcher"))
    }

    @Test
    fun selectsAnswer() {
        val pollId = 1L
        val poll = pollService.findById(pollId, DEFAULT_LOGGED_IN_USER_ID).data!!
        var answerInfo = poll.answerInfo()

        assertEquals(2, answerInfo.size)
        assertTrue(answerInfo.none { it.answerId == TRANSIENT_ID })

        assertTrue(answerInfo.first().answererIds.isEmpty())
        assertEquals(zeroAmount(), answerInfo.first().percentage)
        assertTrue(answerInfo.last().answererIds.isEmpty())
        assertEquals(zeroAmount(), answerInfo.last().percentage)

        val answer1Id = answerInfo.first().answerId
        val answer2Id = answerInfo.last().answerId

        pollService.selectAnswer(SelectPollAnswerRequest(pollId, "85fa0681-b7bd-4ee3-b5b5-eb2672181ae2", answer1Id))
        pollService.selectAnswer(SelectPollAnswerRequest(pollId, "04e51444-85af-4d92-b89a-c8f761b7f3ea", answer1Id))
        pollService.selectAnswer(SelectPollAnswerRequest(pollId, "b41c2dd8-db85-4d96-a1f4-92f90851f7f2", answer1Id))
        answerInfo = pollService.findById(pollId, DEFAULT_LOGGED_IN_USER_ID).data!!.answerInfo()

        assertEquals(3, answerInfo.byId(answer1Id).answererIds.size)
        assertEquals(amount(100.00), answerInfo.byId(answer1Id).percentage)
        assertTrue(answerInfo.byId(answer2Id).answererIds.isEmpty())
        assertEquals(zeroAmount(), answerInfo.byId(answer2Id).percentage)

        pollService.selectAnswer(SelectPollAnswerRequest(pollId, "755520ef-f06a-49e2-af7e-a0f4c19b1aba", answer2Id))
        answerInfo = pollService.findById(pollId, DEFAULT_LOGGED_IN_USER_ID).data!!.answerInfo()

        assertEquals(3, answerInfo.byId(answer1Id).answererIds.size)
        assertEquals(amount(75.00), answerInfo.byId(answer1Id).percentage)
        assertEquals(1, answerInfo.byId(answer2Id).answererIds.size)
        assertEquals(amount(25.00), answerInfo.byId(answer2Id).percentage)

        pollService.selectAnswer(SelectPollAnswerRequest(pollId, "85fa0681-b7bd-4ee3-b5b5-eb2672181ae2", answer2Id))
        answerInfo = pollService.findById(pollId, DEFAULT_LOGGED_IN_USER_ID).data!!.answerInfo()

        assertEquals(2, answerInfo.byId(answer1Id).answererIds.size)
        assertEquals(amount(50.00), answerInfo.byId(answer1Id).percentage)
        assertEquals(2, answerInfo.byId(answer2Id).answererIds.size)
        assertEquals(amount(50.00), answerInfo.byId(answer2Id).percentage)

        pollService.selectAnswer(SelectPollAnswerRequest(pollId, "b41c2dd8-db85-4d96-a1f4-92f90851f7f2", answer1Id))
        answerInfo = pollService.findById(pollId, DEFAULT_LOGGED_IN_USER_ID).data!!.answerInfo()

        assertEquals(1, answerInfo.byId(answer1Id).answererIds.size)
        assertEquals(amount(33.33), answerInfo.byId(answer1Id).percentage)
        assertEquals(2, answerInfo.byId(answer2Id).answererIds.size)
        assertEquals(amount(66.67), answerInfo.byId(answer2Id).percentage)

    }

    @Test
    fun updatesPoll() {
        val poll = pollService.createPoll(
            CreatePollRequest(now(), "5760b116-6aab-4f04-b8be-650e27a85d09", "123456", "What is best game", setOf("Rdr", "Gta"))
        ).data!!

        val errorResponse = pollService.updatePoll(UpdatePollRequest(poll.id, "What is the best video game?", "85fa0681-b7bd-4ee3-b5b5-eb2672181ae2"))
        assertFalse(errorResponse.isSuccessful)
        assertEquals("You can't update this element", errorResponse.message)

        val successResponse = pollService.updatePoll(UpdatePollRequest(poll.id, "What is the best video game?", "5760b116-6aab-4f04-b8be-650e27a85d09"))
        assertTrue(successResponse.isSuccessful)
        assertTrue(pollService.findById(poll.id, DEFAULT_LOGGED_IN_USER_ID).data!!.content.contains("What is the best video game?"))
    }

    @Test
    fun removesAndActivatesPoll() {
        pollService.remove(OperateOnContentRequest(1, "5760b116-6aab-4f04-b8be-650e27a85d09", now()))
        assertTrue(pollService.findById(1, DEFAULT_LOGGED_IN_USER_ID).data!!.isRemoved)
        assertFalse(pollService.findById(1, DEFAULT_LOGGED_IN_USER_ID).data!!.isActive)

        pollService.activate(OperateOnContentRequest(1, "5760b116-6aab-4f04-b8be-650e27a85d09", dateTime("20201-04-05T12:00:00")))
        val poll = pollService.findById(1, DEFAULT_LOGGED_IN_USER_ID).data!!
        assertFalse(poll.isRemoved)
        assertTrue(poll.isActive)
        assertEquals(dateTime("20201-04-05T12:00:00"), poll.updatedAt)
    }

    @Test
    fun addsCommentsToPolls() {
        val pollId = pollService.createPoll(
            CreatePollRequest(now(), "5760b116-6aab-4f04-b8be-650e27a85d09", "123456", "What is best game", setOf("Rdr", "Gta"))
        ).data!!.id
        pollService.addComment(AddCommentRequest(pollId, "85fa0681-b7bd-4ee3-b5b5-eb2672181ae2", "Comment 1", now()))
        pollService.addComment(AddCommentRequest(pollId, "85fa0681-b7bd-4ee3-b5b5-eb2672181ae2", "Comment 1", now()))
        pollService.addComment(AddCommentRequest(pollId, "85fa0681-b7bd-4ee3-b5b5-eb2672181ae2", "Comment 1", now()))
        pollService.addComment(AddCommentRequest(pollId, "85fa0681-b7bd-4ee3-b5b5-eb2672181ae2", "Comment 1", now()))
        pollService.addComment(AddCommentRequest(pollId, "85fa0681-b7bd-4ee3-b5b5-eb2672181ae2", "Comment 1", now())).data!!

        val listCommentsResponse = pollService.listComments(ListCommentsRequest(pollId, DEFAULT_LOGGED_IN_USER_ID, DEFAULT_LISTING_PARAMS))
        assertEquals(5, listCommentsResponse.resultSize)
        assertTrue(listCommentsResponse.data.none { it.id == TRANSIENT_ID })

        val poll = pollService.findById(pollId, DEFAULT_LOGGED_IN_USER_ID).data!!
        assertEquals(5, poll.commentsAmount)
        assertEquals(3, poll.commentsPreview.size)
    }

    private fun DiscussionElementData.answerInfo(): Set<AnswerInfo> = fromJson<PollData>(this.content).answers
    private fun Set<AnswerInfo>.byId(id: Long) = this.first { it.answerId == id }

}