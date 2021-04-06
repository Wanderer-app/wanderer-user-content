package ge.wanderer.web.api.spring.http.controller

import ge.wanderer.common.listing.ListingParams
import ge.wanderer.service.protocol.data.*
import ge.wanderer.service.protocol.interfaces.PostService
import ge.wanderer.service.protocol.request.*
import ge.wanderer.service.protocol.response.ServiceListingResponse
import ge.wanderer.service.protocol.response.ServiceResponse
import ge.wanderer.web.api.spring.http.httpResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("api/posts")
class PostController(
    @Autowired private val postService: PostService
) {

    @PostMapping("/create")
    fun createPost(@RequestBody request: CreatePostRequest): ResponseEntity<ServiceResponse<DiscussionElementData>> =
        httpResponse(postService.createPost(request))

    @PostMapping("/update")
    fun updatePost(@RequestBody request: UpdatePostRequest): ResponseEntity<ServiceResponse<DiscussionElementData>> =
        httpResponse(postService.updatePost(request))

    @GetMapping("/{id}")
    fun getPost(@PathVariable id: Long): ResponseEntity<ServiceResponse<DiscussionElementData>> =
        httpResponse(postService.findById(id))

    @PostMapping("/activate")
    fun activatePost(@RequestBody request: OperateOnContentRequest): ResponseEntity<ServiceResponse<DiscussionElementData>> =
        httpResponse(postService.activate(request))

    @PostMapping("/remove")
    fun removePost(@RequestBody request: OperateOnContentRequest): ResponseEntity<ServiceResponse<DiscussionElementData>> =
        httpResponse(postService.remove(request))

    @PostMapping("/up-vote")
    fun upVote(@RequestBody request: OperateOnContentRequest): ResponseEntity<ServiceResponse<RatingData>> =
        httpResponse(postService.giveUpVote(request))

    @PostMapping("/down-vote")
    fun downVote(@RequestBody request: OperateOnContentRequest): ResponseEntity<ServiceResponse<RatingData>> =
        httpResponse(postService.giveDownVote(request))

    @PostMapping("/remove-vote")
    fun removeVote(@RequestBody request: OperateOnContentRequest): ResponseEntity<ServiceResponse<RatingData>> =
        httpResponse(postService.removeVote(request))

    @PostMapping("/add-comment")
    fun addComment(@RequestBody request: AddCommentRequest): ResponseEntity<ServiceResponse<CommentData>> =
        httpResponse(postService.addComment(request))

    @PostMapping("/{id}/comments")
    fun listComments(@RequestBody listingParams: ListingParams, @PathVariable id: Long): ResponseEntity<ServiceListingResponse<CommentData>> =
        httpResponse(postService.listComments(id, listingParams))

    @PostMapping("/report")
    fun report(@RequestBody request: ReportContentRequest): ResponseEntity<ServiceResponse<ReportData>> =
        httpResponse(postService.report(request))

    @PostMapping("/{id}/reports")
    fun listReports(@PathVariable id: Long): ResponseEntity<ServiceListingResponse<ReportData>> =
        httpResponse(postService.listReportsForContent(id))

}