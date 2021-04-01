package ge.wanderer.persistence.inMemory.repository

import ge.wanderer.common.listing.ListingParams
import ge.wanderer.common.now
import ge.wanderer.core.data.file.AttachedFile
import ge.wanderer.core.integration.user.UserService
import ge.wanderer.core.model.comment.Comment
import ge.wanderer.core.model.comment.IComment
import ge.wanderer.core.model.content.status.Active
import ge.wanderer.core.model.discussion.post.IPost
import ge.wanderer.core.model.discussion.post.Post
import ge.wanderer.core.repository.CommentRepository
import ge.wanderer.core.repository.PostRepository
import ge.wanderer.core.repository.TRANSIENT_ID
import org.joda.time.LocalDateTime
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.util.concurrent.atomic.AtomicLong

@Component
class PostRepositoryImpl(
    @Autowired private val userService: UserService,
    @Autowired private val commentRepository: CommentRepository
): PostRepository, BaseInMemoryRepository<IPost>() {

    override fun data(): HashMap<Long, IPost> = posts
    override fun nextId(): Long = currentId.incrementAndGet()

    private val currentId = AtomicLong(1)
    private val posts = hashMapOf(
        create(1, now(), "123", "aqa mshvidoba", listOf(), setOf(Pair(2L, "Baro baro"), Pair(3L, "zd"))),
        create(2, now(), "1243", "es ra yle marshrutia", listOf(), setOf(Pair(1L, "atrakeb"))),
        create(3, now(), "1223", "aaaaa", listOf(), setOf(Pair(2L, "nomeri dawere"))),
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

        return Pair(id, Post(id, user, createDate, text, routeCode, attachedFiles, Active(createDate, user), comments))
    }

    private fun createComment(userId: Long, text: String, createDate: LocalDateTime): IComment {
        val user = userService.findUserById(userId)
        val comment =  Comment(TRANSIENT_ID, user, createDate, text, Active(createDate, user), mutableListOf(), mutableListOf(), mutableSetOf())
        return commentRepository.persist(comment)
    }

}