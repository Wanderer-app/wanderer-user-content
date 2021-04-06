package ge.wanderer.web.api.spring.http.controller

import ge.wanderer.common.listing.ListingParams
import ge.wanderer.service.protocol.data.DiscussionElementData
import ge.wanderer.service.protocol.data.ReportData
import ge.wanderer.service.protocol.interfaces.ReportsService
import ge.wanderer.service.protocol.request.CreatePollRequest
import ge.wanderer.service.protocol.request.UpdatePollRequest
import ge.wanderer.service.protocol.response.ServiceListingResponse
import ge.wanderer.service.protocol.response.ServiceResponse
import ge.wanderer.web.api.spring.http.httpResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("api/reports")
class ReportController(
    @Autowired private val reportsService: ReportsService
) {

    @PostMapping("/list")
    fun createPoll(@RequestBody listingParams: ListingParams): ResponseEntity<ServiceListingResponse<ReportData>> =
        httpResponse(reportsService.list(listingParams))

    @PostMapping("/{id}/dismiss")
    fun updatePoll(@PathVariable id: Long): ResponseEntity<ServiceResponse<ReportData>> =
        httpResponse(reportsService.dismiss(id))
}