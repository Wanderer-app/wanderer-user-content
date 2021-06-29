package ge.wanderer.web.api.spring.http.controller

import ge.wanderer.common.listing.ListingParams
import ge.wanderer.service.protocol.data.DiscussionElementData
import ge.wanderer.service.protocol.interfaces.DiscussionService
import ge.wanderer.service.protocol.response.ServiceListingResponse
import ge.wanderer.web.api.spring.http.httpResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("api/discussion")
class DiscussionController(
    @Autowired private val discussionService: DiscussionService
) {
    @PostMapping("/for-route/{routeCode}")
    fun listForRoute(
        @PathVariable routeCode: String,
        @RequestBody listingParams: ListingParams,
        @RequestHeader(name = "user-token", required = false) loggedInUserId: String?
    ): ResponseEntity<ServiceListingResponse<DiscussionElementData>> =
        httpResponse(discussionService.getDiscussionForRoute(routeCode, loggedInUserId, listingParams))
}