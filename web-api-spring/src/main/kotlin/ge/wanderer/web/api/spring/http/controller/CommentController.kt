package ge.wanderer.web.api.spring.http.controller

import ge.wanderer.common.listing.ListingParams
import ge.wanderer.service.protocol.data.*
import ge.wanderer.service.protocol.interfaces.CommentService
import ge.wanderer.service.protocol.request.*
import ge.wanderer.service.protocol.response.ServiceListingResponse
import ge.wanderer.service.protocol.response.ServiceResponse
import ge.wanderer.web.api.spring.http.httpResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("api/comments")
class CommentController(
    @Autowired private val commentService: CommentService
) {
    
    @PostMapping("/update")
    fun updateComment(@RequestBody request: UpdateCommentRequest): ResponseEntity<ServiceResponse<CommentData>> =
        httpResponse(commentService.updateComment(request))

    @GetMapping("/{id}")
    fun getComment(
        @PathVariable id: Long,
        @RequestHeader(name = "user-token", required = false) requestingUserId: Long?
    ): ResponseEntity<ServiceResponse<CommentData>> =
        httpResponse(commentService.findById(id, requestingUserId))

    @PostMapping("/activate")
    fun activateComment(@RequestBody request: OperateOnContentRequest): ResponseEntity<ServiceResponse<CommentData>> =
        httpResponse(commentService.activate(request))

    @PostMapping("/remove")
    fun removeComment(@RequestBody request: OperateOnContentRequest): ResponseEntity<ServiceResponse<CommentData>> =
        httpResponse(commentService.remove(request))

    @PostMapping("/up-vote")
    fun upVote(@RequestBody request: OperateOnContentRequest): ResponseEntity<ServiceResponse<RatingData>> =
        httpResponse(commentService.giveUpVote(request))

    @PostMapping("/down-vote")
    fun downVote(@RequestBody request: OperateOnContentRequest): ResponseEntity<ServiceResponse<RatingData>> =
        httpResponse(commentService.giveDownVote(request))

    @PostMapping("/remove-vote")
    fun removeVote(@RequestBody request: OperateOnContentRequest): ResponseEntity<ServiceResponse<RatingData>> =
        httpResponse(commentService.removeVote(request))

    @PostMapping("/add-reply")
    fun addComment(@RequestBody request: AddCommentRequest): ResponseEntity<ServiceResponse<CommentData>> =
        httpResponse(commentService.addComment(request))

    @PostMapping("/{contentId}/replies")
    fun listComments(
        @RequestBody listingParams: ListingParams,
        @PathVariable contentId: Long,
        @RequestHeader(name = "user-token", required = false) loggedInUserId: Long?
    ): ResponseEntity<ServiceListingResponse<CommentData>> =
        httpResponse(commentService.listComments(ListCommentsRequest(contentId, loggedInUserId, listingParams)))

    @PostMapping("/report")
    fun report(@RequestBody request: ReportContentRequest): ResponseEntity<ServiceResponse<ReportData>> =
        httpResponse(commentService.report(request))

    @PostMapping("/{id}/reports")
    fun listReports(@PathVariable id: Long): ResponseEntity<ServiceListingResponse<ReportData>> =
        httpResponse(commentService.listReportsForContent(id))
}