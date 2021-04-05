package ge.wanderer.service.spring.impl

import ge.wanderer.common.amount
import ge.wanderer.common.dateTime
import ge.wanderer.common.enums.UserContentType
import ge.wanderer.common.now
import ge.wanderer.core.model.content.status.Active
import ge.wanderer.core.model.discussion.poll.PollAnswer
import ge.wanderer.persistence.listing.ListingParams
import ge.wanderer.persistence.repository.CommentRepository
import ge.wanderer.persistence.repository.PollRepository
import ge.wanderer.service.protocol.request.*
import ge.wanderer.service.spring.command.CommandProvider
import ge.wanderer.service.spring.test_support.*
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class PollServiceImplTest {

    private val userService = mockedUserService()
    private val commentPreviewProvider = testCommentPreviewProvider()
    private val pollRepository = mockk<PollRepository>()
    private val commentRepository = mockk<CommentRepository>()

    private val service = PollServiceImpl(userService, commentPreviewProvider, CommandProvider(), pollRepository, commentRepository)

    @Test
    fun correctlyCreatesPoll() {
        every { pollRepository.persist(any()) } answers { arg(0) }
        val request = CreatePollRequest(now(), 1, "123", "Some question", setOf("Answer 1", "Answer 2"))
        val response = service.createPoll(request)

        assertTrue(response.isSuccessful)
        assertEquals("Poll created!. New model persisted successfully", response.message)

        val data = response.data!!
        assertEquals("123", data.routeCode)
        assertEquals(jambura().id, data.creator.id)
        assertTrue(data.isActive)
        assertTrue(data.content.contains("Some question"))
        assertTrue(data.content.contains("Answer 1"))
        assertTrue(data.content.contains("Answer 2"))
        assertTrue(data.attachedFiles.isEmpty())
        assertEquals(UserContentType.POLL, data.type)

        verify(exactly = 1) { userService.findUserById(1) }
        verify(exactly = 1) { pollRepository.persist(any()) }
    }

    @Test
    fun correctlyHandlesErrorOnCommandExecution() {
        val request = CreatePollRequest(now(), 1, "123", "Some question", setOf())
        val response = service.createPoll(request)

        assertFalse(response.isSuccessful)
        assertEquals("Poll must have at least 2 answers!", response.message)
    }

    @Test
    fun correctlyUpdatesPoll() {
        every { pollRepository.findById(1) } returns pollWithAnswers(1, jangula(), now(), "123", "Some question", mutableSetOf("Answer 1", "Answer 2"))
        val request = UpdatePollRequest(1, "Pick one", 3)
        val response = service.updatePoll(request)

        assertTrue(response.isSuccessful)
        assertEquals("POLL updated", response.message)
        assertTrue(response.data!!.content.contains("Pick one"))
    }

    @Test
    fun correctlyAddsAnswerToPoll() {
        every { pollRepository.findById(1) } returns pollWithAnswers(1, jangula(), now(), "123", "Some question", mutableSetOf("Answer 1", "Answer 2"))
        val request = AddAnswerToPollRequest(1, 3, "Answer 3", now())
        val response = service.addAnswer(request)

        assertTrue(response.isSuccessful)
        assertEquals("Answer added!", response.message)
        assertTrue(response.data!!.content.contains("Answer 3"))
    }

    @Test
    fun correctlyRemovesPollAnswer() {
        val answer1 = PollAnswer(1, "answer 1", now(), jambura(), Active(now(), jambura()), mutableSetOf())
        val answer2 = PollAnswer(2, "answer 2", now(), jambura(), Active(now(), jambura()), mutableSetOf())
        val answer3 = PollAnswer(3, "answer 3", now(), jambura(), Active(now(), jambura()), mutableSetOf())
        val poll = createPoll(1, jambura(), now(), "123", "aaa", mutableSetOf(answer1, answer2, answer3))

        every { pollRepository.findById(1) } returns poll

        val request = RemovePollAnswerRequest(1, 2, 3, now())
        val response = service.removeAnswer(request)

        assertTrue(response.isSuccessful)
        assertEquals("Answer Removed!", response.message)
        assertFalse(response.data!!.content.contains("answer 2"))
        assertEquals(2, poll.answers().size)
        assertTrue(answer2.isRemoved())
        verify(exactly = 1) { userService.notifyContentStatusChange(answer2) }
    }

    @Test
    fun correctlySelectsAnswer() {
        val answer1 = pollAnswer(1, "Answer 1", now(), mutableSetOf(), jambura())
        val answer2 = pollAnswer(2, "Answer 2", now(), mutableSetOf(), jambura())
        val poll = createPoll(1, jambura(), now(), "123", "Some question", mutableSetOf(answer1, answer2))
        every { pollRepository.findById(1) } returns poll

        var request = SelectPollAnswerRequest(1, 4, 1)
        var response = service.selectAnswer(request)
        assertTrue(response.isSuccessful)
        assertEquals("Answer Selected", response.message)
        assertTrue(response.data!!.content.contains("4"))

        val answer1Data = poll.answersData().first { it.title == "Answer 1" }
        assertEquals(amount(100), answer1Data.percentage)
        assertEquals(4, answer1Data.answererIds[0])

        request = SelectPollAnswerRequest(1, 5, 2)
        response = service.selectAnswer(request)
        assertTrue(response.isSuccessful)

        val answer2Data = poll.answersData().first { it.title == "Answer 2" }
        assertEquals(amount(50), answer2Data.percentage)
        assertEquals(5, answer2Data.answererIds[0])
        assertEquals(amount(50), poll.answersData().first { it.title == "Answer 1" }.percentage)

        service.selectAnswer(SelectPollAnswerRequest(1, 4, 1))
        assertEquals(amount(0), poll.answersData().first { it.title == "Answer 1" }.percentage)
        assertEquals(amount(100), poll.answersData().first { it.title == "Answer 2" }.percentage)

    }

    @Test
    fun correctlyFindsPollById() {
        every { pollRepository.findById(1) } returns pollWithAnswers(1, jangula(), now(), "123", "Some question", mutableSetOf("Answer 1", "Answer 2"))

        val response = service.findById(1)
        assertTrue(response.isSuccessful)
        assertEquals("Poll fetched", response.message)

        val data = response.data!!
        assertEquals(1, data.id)
        assertNull(data.ratingData)
        assertEquals(jangula().id, data.creator.id)
    }

    @Test
    fun correctlyActivatesAPoll() {
        val poll = pollWithAnswers(1, jangula(), now(), "123", "Some question", mutableSetOf("Answer 1", "Answer 2"))
        poll.remove(now(), jambura())
        every { pollRepository.findById(1) } returns poll

        val request = OperateOnContentRequest(1, 1, dateTime("2021-03-28T16:00:00"))
        val response = service.activate(request)

        assertTrue(response.isSuccessful)
        assertEquals("POLL activated successfully!", response.message)
        assertTrue(response.data!!.isActive)
        assertFalse(response.data!!.isRemoved)
        assertEquals(dateTime("2021-03-28T16:00:00"), response.data!!.updatedAt)
    }

    @Test
    fun correctlyRemovesAPoll() {
        val poll = pollWithAnswers(1, jangula(), now(), "123", "Some question", mutableSetOf("Answer 1", "Answer 2"))
        every { pollRepository.findById(1) } returns poll

        val request = OperateOnContentRequest(1, 1, dateTime("2021-03-28T16:00:00"))
        val response = service.remove(request)

        assertTrue(response.isSuccessful)
        assertEquals("POLL removed successfully!", response.message)
        assertFalse(response.data!!.isActive)
        assertTrue(response.data!!.isRemoved)
        assertEquals(dateTime("2021-03-28T16:00:00"), response.data!!.updatedAt)
        verify(exactly = 1) { userService.notifyContentStatusChange(poll) }
    }

    @Test
    fun addsCommentToPoll() {
        every { pollRepository.findById(1) } returns pollWithAnswers(1, jangula(), now(), "123", "Some question", mutableSetOf("Answer 1", "Answer 2"))

        val request = AddCommentRequest(1, 1, "gaparchanavebs?", now())
        val response = service.addComment(request)

        assertTrue(response.isSuccessful)
        assertEquals("Comment added", response.message)
        assertEquals("gaparchanavebs?", response.data!!.text)
        assertEquals(jambura().id, response.data!!.author.id)
        verify(exactly = 1) { userService.notifyContentWasCommented(any(), any()) }
    }

    @Test
    fun listsComments() {
        val poll = pollWithAnswers(1, kalduna(), now(), "123", "Some question", mutableSetOf("Answer 1", "Answer 2"))
        every { pollRepository.findById(1) } returns poll
        every { commentRepository.listActiveFor(poll, any()) } returns listOf(
            createNewComment(1, now(), "aaaa", jambura()),
            createNewComment(2, now(), "shari ginda?", jangula()),
            createNewComment(1, now(), "she shakalo", patata()),
            createNewComment(1, now(), "gvipasuxe sabaka", vipiSoxumski())
        )

        val response = service.listComments(1, ListingParams(4, 1, null, listOf()))
        assertTrue(response.isSuccessful)
        assertEquals("Comments fetched!", response.message)
        assertEquals(4, response.resultSize)
        assertEquals(4, response.data.size)
    }
}