package ge.wanderer.web.api.spring.http.controller

import ge.wanderer.common.listing.ListingParams
import ge.wanderer.service.protocol.data.CommentData
import ge.wanderer.service.protocol.data.DiscussionElementData
import ge.wanderer.service.protocol.interfaces.PollService
import ge.wanderer.service.protocol.request.*
import ge.wanderer.service.protocol.response.ServiceListingResponse
import ge.wanderer.service.protocol.response.ServiceResponse
import ge.wanderer.web.api.spring.http.httpResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("api/polls")
class PollController(
    @Autowired private val pollService: PollService
) {

    @PostMapping("/create")
    fun createPoll(@RequestBody request: CreatePollRequest): ResponseEntity<ServiceResponse<DiscussionElementData>> =
        httpResponse(pollService.createPoll(request))

    @PostMapping("/update")
    fun updatePoll(@RequestBody request: UpdatePollRequest): ResponseEntity<ServiceResponse<DiscussionElementData>> =
        httpResponse(pollService.updatePoll(request))

    @PostMapping("/add-answer")
    fun addAnswer(@RequestBody request: AddAnswerToPollRequest): ResponseEntity<ServiceResponse<DiscussionElementData>> =
        httpResponse(pollService.addAnswer(request))

    @PostMapping("/remove-answer")
    fun removeAnswer(@RequestBody request: RemovePollAnswerRequest): ResponseEntity<ServiceResponse<DiscussionElementData>> =
        httpResponse(pollService.removeAnswer(request))

    @PostMapping("/select-answer")
    fun selectAnswer(@RequestBody request: SelectPollAnswerRequest): ResponseEntity<ServiceResponse<DiscussionElementData>> =
        httpResponse(pollService.selectAnswer(request))

    @GetMapping("/{id}")
    fun getPoll(@PathVariable id: Long, @RequestHeader(name = "loggedInUserId", required = true) loggedInUserId: Long): ResponseEntity<ServiceResponse<DiscussionElementData>> =
        httpResponse(pollService.findById(id, loggedInUserId))

    @PostMapping("/activate")
    fun activatePoll(@RequestBody request: OperateOnContentRequest): ResponseEntity<ServiceResponse<DiscussionElementData>> =
        httpResponse(pollService.activate(request))

    @PostMapping("/remove")
    fun removePoll(@RequestBody request: OperateOnContentRequest): ResponseEntity<ServiceResponse<DiscussionElementData>> =
        httpResponse(pollService.remove(request))

    @PostMapping("/add-comment")
    fun addComment(@RequestBody request: AddCommentRequest): ResponseEntity<ServiceResponse<CommentData>> =
        httpResponse(pollService.addComment(request))

    @PostMapping("/{contentId}/comments")
    fun listComments(
        @RequestBody listingParams: ListingParams,
        @PathVariable contentId: Long,
        @RequestHeader(name = "loggedInUserId", required = true) loggedInUserId: Long
    ): ResponseEntity<ServiceListingResponse<CommentData>> =
        httpResponse(pollService.listComments(ListCommentsRequest(contentId, loggedInUserId, listingParams)))

}