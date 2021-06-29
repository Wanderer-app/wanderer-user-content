package ge.wanderer.persistence.inMemory.repository

import ge.wanderer.common.constants.TRANSIENT_ID
import ge.wanderer.common.now
import ge.wanderer.core.integration.file.AttachedFile
import ge.wanderer.core.integration.user.UserService
import ge.wanderer.core.model.comment.Comment
import ge.wanderer.core.model.comment.IComment
import ge.wanderer.core.model.content.status.Active
import ge.wanderer.core.model.discussion.post.IPost
import ge.wanderer.core.model.discussion.post.Post
import ge.wanderer.persistence.inMemory.model.InMemoryPost
import ge.wanderer.persistence.inMemory.sorting.SequenceSorter
import ge.wanderer.persistence.repository.CommentRepository
import ge.wanderer.persistence.repository.PostRepository
import org.joda.time.LocalDateTime
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.util.concurrent.atomic.AtomicLong

@Component
class PostRepositoryImpl(
    @Autowired private val userService: UserService,
    @Autowired private val commentRepository: CommentRepository,
    @Autowired private val sorter: SequenceSorter<IPost>
): PostRepository, BaseInMemoryRepository<IPost>(sorter) {

    override fun data(): HashMap<Long, IPost> = posts
    override fun nextId(): Long = currentId.getAndIncrement()

    private val currentId = AtomicLong(1)
    private val posts = hashMapOf(
        create("5760b116-6aab-4f04-b8be-650e27a85d09", now(), "TB201301", "აქა მშვიდობა", listOf(), setOf(Pair("85fa0681-b7bd-4ee3-b5b5-eb2672181ae2", "ბარო ბარო"), Pair("04e51444-85af-4d92-b89a-c8f761b7f3ea", "zd"))),
        create("85fa0681-b7bd-4ee3-b5b5-eb2672181ae2", now(), "TB201301", "ეს რა ცუდი მარშრუტია", listOf(), setOf(Pair("5760b116-6aab-4f04-b8be-650e27a85d09", "მეც არ მამეწონა"))),
        create("04e51444-85af-4d92-b89a-c8f761b7f3ea", now(), "TB201301", "ააააა", listOf(), setOf(Pair("85fa0681-b7bd-4ee3-b5b5-eb2672181ae2", "ზახრუმა"))),
        create("b41c2dd8-db85-4d96-a1f4-92f90851f7f2", now(), "TB201301", "აქ უნდა წავიდე", listOf(), setOf(Pair("5760b116-6aab-4f04-b8be-650e27a85d09", "მეც წამიყვანე პლზ"))),
        create("755520ef-f06a-49e2-af7e-a0f4c19b1aba", now(), "TB201301", "სალამი", listOf(), setOf(Pair("b41c2dd8-db85-4d96-a1f4-92f90851f7f2", "სალამი ბიჭებს ტრუსიკი გვიჭერს")))
    )

    private fun create(
        userId: String,
        createDate: LocalDateTime,
        routeCode: String,
        text: String,
        attachedFiles: List<AttachedFile>,
        commentsData: Set<Pair<String, String>>
    ): Pair<Long, IPost> {
        val id = currentId.getAndIncrement()
        val user = userService.findUserById(userId)
        val comments = commentsData.map { createComment(it.first, it.second, createDate) }.toMutableList()

        return Pair(
            id,
            InMemoryPost(id, Post(id, user, createDate, text, routeCode, attachedFiles, Active(createDate, user), comments), commentRepository)
        )
    }

    private fun createComment(userId: String, text: String, createDate: LocalDateTime): IComment {
        val user = userService.findUserById(userId)
        val comment =  Comment(TRANSIENT_ID, user, createDate, text, Active(createDate, user))
        return commentRepository.persist(comment)
    }

    override fun makePersistent(model: IPost, id: Long): IPost
            = InMemoryPost(id, model, commentRepository)
}