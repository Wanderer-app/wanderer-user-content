package ge.wanderer.persistence.inMemory.repository

import ge.wanderer.common.constants.TRANSIENT_ID
import ge.wanderer.common.enums.PinType
import ge.wanderer.common.enums.PinType.*
import ge.wanderer.common.map.LatLng
import ge.wanderer.common.now
import ge.wanderer.core.data.file.AttachedFile
import ge.wanderer.core.integration.user.UserService
import ge.wanderer.core.model.comment.Comment
import ge.wanderer.core.model.comment.IComment
import ge.wanderer.core.model.content.status.Active
import ge.wanderer.core.model.map.IPin
import ge.wanderer.core.model.map.Pin
import ge.wanderer.core.model.map.PinContent
import ge.wanderer.persistence.inMemory.model.InMemoryPin
import ge.wanderer.common.listing.ListingParams
import ge.wanderer.persistence.inMemory.sorting.PinSorter
import ge.wanderer.persistence.repository.CommentRepository
import ge.wanderer.persistence.repository.PinRepository
import org.joda.time.LocalDateTime
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.util.concurrent.atomic.AtomicLong

@Component
class PinRepositoryImpl(
    @Autowired private val userService: UserService,
    @Autowired private val commentRepository: CommentRepository,
    @Autowired private val sorter: PinSorter
): PinRepository, BaseInMemoryRepository<IPin>(sorter) {

    override fun listForRoute(routeCode: String, listingParams: ListingParams): List<IPin> =
        pins.values
            .asSequence()
            .filter { it.isActive() }
            .filter { it.routeCode() == routeCode }
            .applyListingParams(listingParams)

    override fun data(): HashMap<Long, IPin> = pins
    override fun nextId(): Long = currentId.getAndIncrement()

    private val currentId = AtomicLong(1)
    private val pins = hashMapOf(
        create(1, now(), "123", "aqa prtxilad", "volkebi dadian", null, LatLng(1f, 1f), WARNING, setOf(Pair(2L, "ragacas atrakeb"))),
        create(2, now(), "1234", "mewyeri chamowva", "ver gaivlit", null, LatLng(2f, 1f), TIP, setOf(Pair(1L, "dges vnaxe da agaraa"))),
        create(1, now(), "1235", "dasasvenebeli adgili", "banakistvis kai adgilia", null, LatLng(1f, 2f), RESTING_PLACE, setOf(Pair(1L, "martalia es kaci"))),
        create(4, now(), "123", "tesli adgili", "kai xedia aqedan", AttachedFile(), LatLng(3f, 1f), SIGHT, setOf(Pair(6L, "diax diax"))),
        create(5, now(), "1234", "aqa prtxilad", "datvebi dadian", null, LatLng(1f, 3f), TIP, setOf(Pair(2L, "ragacas atrakeb")))
    )

    private fun create(
        userId: Long,
        createDate: LocalDateTime,
        routeCode: String,
        title: String,
        text: String,
        attachedFile: AttachedFile?,
        location: LatLng,
        type: PinType,
        commentsData: Set<Pair<Long, String>>
    ): Pair<Long, IPin> {
        val id = currentId.getAndIncrement()
        val user = userService.findUserById(userId)
        val comments = commentsData.map { createComment(it.first, it.second, createDate) }.toMutableList()
        val content = PinContent(title, text, attachedFile)

        return Pair(
            id,
            InMemoryPin(id, Pin(id, user, createDate, location, routeCode, type, content, Active(createDate, user), comments), commentRepository)
        )
    }

    private fun createComment(userId: Long, text: String, createDate: LocalDateTime): IComment {
        val user = userService.findUserById(userId)
        val comment =  Comment(TRANSIENT_ID, user, createDate, text, Active(createDate, user))
        return commentRepository.persist(comment)
    }

    override fun makePersistent(model: IPin, id: Long): IPin = InMemoryPin(id, model, commentRepository)

}