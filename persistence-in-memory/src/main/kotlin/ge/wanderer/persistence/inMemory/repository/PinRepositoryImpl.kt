package ge.wanderer.persistence.inMemory.repository

import ge.wanderer.common.constants.TRANSIENT_ID
import ge.wanderer.common.enums.PinType
import ge.wanderer.common.enums.PinType.*
import ge.wanderer.common.map.LatLng
import ge.wanderer.common.now
import ge.wanderer.core.integration.file.AttachedFile
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
import ge.wanderer.persistence.repository.ReportRepository
import org.joda.time.LocalDateTime
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.util.concurrent.atomic.AtomicLong

@Component
class PinRepositoryImpl(
    @Autowired private val userService: UserService,
    @Autowired private val commentRepository: CommentRepository,
    @Autowired private val reportRepository: ReportRepository,
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
        create("5760b116-6aab-4f04-b8be-650e27a85d09", now(), "TB201301", "აქა ფრთხილად", "ვოლკები დადიან", null, LatLng(41.81309452631177f, 44.84830571006798f), WARNING, setOf(Pair("85fa0681-b7bd-4ee3-b5b5-eb2672181ae2", "რაღაცას გვატყუებ"))),
        create("85fa0681-b7bd-4ee3-b5b5-eb2672181ae2", now(), "TB201301", "მეწყერი ჩამოწვა", "ვერ გაივლით", null, LatLng(41.80877688066833f, 44.858985100557675f), TIP, setOf(Pair("5760b116-6aab-4f04-b8be-650e27a85d09", "დღეს ვნახე და აღარაა"))),
        create("5760b116-6aab-4f04-b8be-650e27a85d09", now(), "TB201301", "დასასვენებელი ადგილი", "ბანაკისთვის კაი ადგილია. ჭაჭას დალევ და გაითიშები", null, LatLng(41.801802130108065f, 44.87436234552735f), RESTING_PLACE, setOf(Pair("5760b116-6aab-4f04-b8be-650e27a85d09", "მართალია ეს კაცი"))),
        create("b41c2dd8-db85-4d96-a1f4-92f90851f7f2", now(), "TB201301", "მაგარი ადგილი", "კაი ხედია აქედან", AttachedFile("user-images/1622887057360-butterfly.jpg"), LatLng(41.8055665996977f, 44.86248552819858f), SIGHT, setOf(Pair("5673a717-9083-4150-8b7e-c3fb25675e3a", "დიახ დიახ"))),
        create("755520ef-f06a-49e2-af7e-a0f4c19b1aba", now(), "TB201301", "ოო იეე", "აქ პირველად ვაკოცე გოგოს", null, LatLng(41.81216881837967f, 44.853783440245074f), TIP, setOf(Pair("85fa0681-b7bd-4ee3-b5b5-eb2672181ae2", "fefefafafa")))
    )

    private fun create(
        userId: String,
        createDate: LocalDateTime,
        routeCode: String,
        title: String,
        text: String,
        attachedFile: AttachedFile?,
        location: LatLng,
        type: PinType,
        commentsData: Set<Pair<String, String>>
    ): Pair<Long, IPin> {
        val id = currentId.getAndIncrement()
        val user = userService.findUserById(userId)
        val comments = commentsData.map { createComment(it.first, it.second, createDate) }.toMutableList()
        val content = PinContent(title, text, attachedFile)

        return Pair(
            id,
            InMemoryPin(id, Pin(id, user, createDate, location, routeCode, type, content, Active(createDate, user), comments), commentRepository, reportRepository)
        )
    }

    private fun createComment(userId: String, text: String, createDate: LocalDateTime): IComment {
        val user = userService.findUserById(userId)
        val comment =  Comment(TRANSIENT_ID, user, createDate, text, Active(createDate, user))
        return commentRepository.persist(comment)
    }

    override fun makePersistent(model: IPin, id: Long): IPin = InMemoryPin(id, model, commentRepository, reportRepository)

}