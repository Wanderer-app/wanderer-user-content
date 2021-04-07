package ge.wanderer.persistence.inMemory.repository

import ge.wanderer.common.constants.TRANSIENT_ID
import ge.wanderer.common.now
import ge.wanderer.core.integration.user.UserService
import ge.wanderer.core.model.comment.Comment
import ge.wanderer.core.model.comment.IComment
import ge.wanderer.core.model.content.status.Active
import ge.wanderer.core.model.discussion.poll.IPoll
import ge.wanderer.core.model.discussion.poll.IPollAnswer
import ge.wanderer.core.model.discussion.poll.Poll
import ge.wanderer.core.model.discussion.poll.PollAnswer
import ge.wanderer.persistence.inMemory.model.InMemoryPoll
import ge.wanderer.persistence.repository.CommentRepository
import ge.wanderer.persistence.repository.PollRepository
import org.joda.time.LocalDateTime
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.util.concurrent.atomic.AtomicLong

@Component
class PollRepositoryImpl(
    @Autowired private val commentRepository: CommentRepository,
    @Autowired private val userService: UserService
): PollRepository, BaseInMemoryRepository<IPoll>() {

    override fun data(): HashMap<Long, IPoll> = polls
    override fun nextId(): Long = currentId.getAndIncrement()

    private val currentId = AtomicLong(1)
    private val pollAnswerCurrentId = AtomicLong(1)

    private val polls = hashMapOf(
        create(1, now(), "123", "Some question 1", setOf("answer 1", "answer 2"), setOf(Pair(2L, "Nice"))),
        create(2, now(), "123", "Some question 2", setOf("answer 1", "answer 2"), setOf(Pair(3L, "Absolutely disgusting"))),
        create(3, now(), "1234", "Some question 3", setOf("answer 1", "answer 2"), setOf(Pair(1L, "gamo iagaze bijo")))
    )

    private fun create(
        userId: Long,
        createDate: LocalDateTime,
        routeCode: String,
        question: String,
        answerTexts: Set<String>,
        commentIds: Set<Pair<Long, String>>
    ): Pair<Long, IPoll> {
        val id = currentId.getAndIncrement()
        val user = userService.findUserById(userId)
        val comments = commentIds.map { createComment(it.first, it.second, createDate) }.toMutableList()

        val answers: MutableSet<IPollAnswer> = answerTexts
            .map { PollAnswer(pollAnswerCurrentId.getAndIncrement(), it, createDate, user, Active(createDate, user)) }
            .toMutableSet()

        return Pair(
            id,
            InMemoryPoll(id, Poll(id, user, createDate, Active(createDate, user), routeCode, question, answers, comments), this, commentRepository)
        )
    }

    private fun createComment(userId: Long, text: String, createDate: LocalDateTime): IComment {
        val user = userService.findUserById(userId)
        val comment =  Comment(TRANSIENT_ID, user, createDate, text, Active(createDate, user))
        return commentRepository.persist(comment)
    }

    fun makeAnswerPersistent(answer: IPollAnswer): IPollAnswer {
        return PollAnswer(pollAnswerCurrentId.getAndIncrement(), answer.text(), answer.createdAt(), answer.creator(), Active(answer.createdAt(), answer.creator()))
    }

    override fun makePersistent(model: IPoll, id: Long): IPoll {
        val answers = model.answers()
        answers.forEach { it.remove(model.createdAt(), model.creator()) }
        answers
            .map { makeAnswerPersistent(it) }
            .forEach { model.addAnswer(it) }

       return InMemoryPoll(id, model, this, commentRepository)
    }

}