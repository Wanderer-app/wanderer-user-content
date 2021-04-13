package ge.wanderer.persistence.inMemory.repository

import ge.wanderer.common.constants.TRANSIENT_ID
import ge.wanderer.common.listing.SortingParams
import ge.wanderer.common.now
import ge.wanderer.core.data.file.AttachedFile
import ge.wanderer.core.integration.user.UserService
import ge.wanderer.core.model.comment.Comment
import ge.wanderer.core.model.comment.IComment
import ge.wanderer.core.model.content.status.Active
import ge.wanderer.core.model.discussion.poll.IPoll
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
        create(1, now(), "123", "aqa mshvidoba", listOf(), setOf(Pair(2L, "Baro baro"), Pair(3L, "zd"))),
        create(2, now(), "1243", "es ra yle marshrutia", listOf(), setOf(Pair(1L, "atrakeb"))),
        create(3, now(), "123", "aaaaa", listOf(), setOf(Pair(2L, "nomeri dawere"))),
        create(4, now(), "1234", "aq unda wavide", listOf(), setOf(Pair(1L, "mec wamiyvane raaa"))),
        create(5, now(), "1273", "salami", listOf(), setOf(Pair(4L, "salami bijebs truiki gvichers")))
    )

    private fun create(
        userId: Long,
        createDate: LocalDateTime,
        routeCode: String,
        text: String,
        attachedFiles: List<AttachedFile>,
        commentsData: Set<Pair<Long, String>>
    ): Pair<Long, IPost> {
        val id = currentId.getAndIncrement()
        val user = userService.findUserById(userId)
        val comments = commentsData.map { createComment(it.first, it.second, createDate) }.toMutableList()

        return Pair(
            id,
            InMemoryPost(id, Post(id, user, createDate, text, routeCode, attachedFiles, Active(createDate, user), comments), commentRepository)
        )
    }

    private fun createComment(userId: Long, text: String, createDate: LocalDateTime): IComment {
        val user = userService.findUserById(userId)
        val comment =  Comment(TRANSIENT_ID, user, createDate, text, Active(createDate, user))
        return commentRepository.persist(comment)
    }

    override fun makePersistent(model: IPost, id: Long): IPost
            = InMemoryPost(id, model, commentRepository)
}