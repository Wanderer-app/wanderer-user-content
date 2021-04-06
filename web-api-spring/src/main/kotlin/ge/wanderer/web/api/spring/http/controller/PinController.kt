package ge.wanderer.web.api.spring.http.controller

import ge.wanderer.common.listing.ListingParams
import ge.wanderer.service.protocol.data.*
import ge.wanderer.service.protocol.interfaces.PinService
import ge.wanderer.service.protocol.request.*
import ge.wanderer.service.protocol.response.ServiceListingResponse
import ge.wanderer.service.protocol.response.ServiceResponse
import ge.wanderer.web.api.spring.http.httpResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("api/pins")
class PinController(
    @Autowired private val pinService: PinService
) {

    @PostMapping("/create")
    fun createPin(@RequestBody request: CreatePinRequest): ResponseEntity<ServiceResponse<PinData>> =
        httpResponse(pinService.createPin(request))

    @PostMapping("/for-route/{routeCode}")
    fun listForRoute(@PathVariable routeCode: String, @RequestBody listingParams: ListingParams): ResponseEntity<ServiceListingResponse<PinMapData>> =
        httpResponse(pinService.listForRoute(routeCode, listingParams))

    @PostMapping("/list")
    fun list(@RequestBody listingParams: ListingParams): ResponseEntity<ServiceListingResponse<PinData>> =
        httpResponse(pinService.list(listingParams))

    @PostMapping("/report-irrelevant")
    fun reportIrrelevant(@RequestBody request: OperateOnContentRequest): ResponseEntity<ServiceResponse<PinData>> =
        httpResponse(pinService.reportIrrelevant(request))

    @PostMapping("/update")
    fun updatePin(@RequestBody request: UpdatePinRequest): ResponseEntity<ServiceResponse<PinData>> =
        httpResponse(pinService.updatePin(request))

    @GetMapping("/{id}")
    fun getPin(@PathVariable id: Long): ResponseEntity<ServiceResponse<PinData>> =
        httpResponse(pinService.findById(id))

    @PostMapping("/activate")
    fun activatePin(@RequestBody request: OperateOnContentRequest): ResponseEntity<ServiceResponse<PinData>> =
        httpResponse(pinService.activate(request))

    @PostMapping("/remove")
    fun removePin(@RequestBody request: OperateOnContentRequest): ResponseEntity<ServiceResponse<PinData>> =
        httpResponse(pinService.remove(request))

    @PostMapping("/up-vote")
    fun upVote(@RequestBody request: OperateOnContentRequest): ResponseEntity<ServiceResponse<RatingData>> =
        httpResponse(pinService.giveUpVote(request))

    @PostMapping("/down-vote")
    fun downVote(@RequestBody request: OperateOnContentRequest): ResponseEntity<ServiceResponse<RatingData>> =
        httpResponse(pinService.giveDownVote(request))

    @PostMapping("/remove-vote")
    fun removeVote(@RequestBody request: OperateOnContentRequest): ResponseEntity<ServiceResponse<RatingData>> =
        httpResponse(pinService.removeVote(request))

    @PostMapping("/add-comment")
    fun addComment(@RequestBody request: AddCommentRequest): ResponseEntity<ServiceResponse<CommentData>> =
        httpResponse(pinService.addComment(request))

    @PostMapping("/{id}/comments")
    fun listComments(@RequestBody listingParams: ListingParams, @PathVariable id: Long): ResponseEntity<ServiceListingResponse<CommentData>> =
        httpResponse(pinService.listComments(id, listingParams))

    @PostMapping("/report")
    fun report(@RequestBody request: ReportContentRequest): ResponseEntity<ServiceResponse<ReportData>> =
        httpResponse(pinService.report(request))

    @PostMapping("/{id}/reports")
    fun listReports(@PathVariable id: Long): ResponseEntity<ServiceListingResponse<ReportData>> =
        httpResponse(pinService.listReportsForContent(id))

}