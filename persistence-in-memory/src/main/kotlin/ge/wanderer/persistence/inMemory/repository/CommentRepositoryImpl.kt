package ge.wanderer.persistence.inMemory.repository

import ge.wanderer.core.model.comment.IComment
import ge.wanderer.core.model.content.CommentableContent
import ge.wanderer.persistence.inMemory.model.InMemoryComment
import ge.wanderer.common.listing.ListingParams
import ge.wanderer.common.listing.SortingParams
import ge.wanderer.core.model.map.IPin
import ge.wanderer.persistence.inMemory.sorting.SequenceSorter
import ge.wanderer.persistence.repository.CommentRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.util.concurrent.atomic.AtomicLong

@Component
class CommentRepositoryImpl(
    @Autowired private val sorters: SequenceSorter<IComment>
): CommentRepository, BaseInMemoryRepository<IComment>(sorters) {

    override fun data(): HashMap<Long, IComment> = comments
    override fun nextId(): Long = currentId.getAndIncrement()
    override fun makePersistent(model: IComment, id: Long): IComment =
        InMemoryComment(id, model, this)
    override fun listActiveFor(content: CommentableContent, listingParams: ListingParams): List<IComment> =
        content.comments()
            .filter { it.isActive() }
            .applyListingParams(listingParams)

    private val currentId = AtomicLong(1)
    private val comments = hashMapOf<Long, IComment>()

}