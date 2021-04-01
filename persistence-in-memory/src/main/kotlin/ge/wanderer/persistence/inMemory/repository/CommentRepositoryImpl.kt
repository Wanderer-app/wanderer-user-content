package ge.wanderer.persistence.inMemory.repository

import ge.wanderer.common.listing.ListingParams
import ge.wanderer.core.model.comment.IComment
import ge.wanderer.core.model.content.CommentableContent
import ge.wanderer.core.repository.CommentRepository
import org.springframework.stereotype.Component
import java.util.concurrent.atomic.AtomicLong

@Component
class CommentRepositoryImpl: CommentRepository, BaseInMemoryRepository<IComment>() {

    override fun data(): HashMap<Long, IComment> = comments
    override fun nextId(): Long = currentId.getAndIncrement()

    override fun listActiveFor(content: CommentableContent, listingParams: ListingParams): List<IComment> =
        content.comments()
            .filter { it.isActive() }

    private val currentId = AtomicLong(1)
    private val comments = hashMapOf<Long, IComment>()

}