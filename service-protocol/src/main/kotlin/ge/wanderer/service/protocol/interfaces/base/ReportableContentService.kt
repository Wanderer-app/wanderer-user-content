package ge.wanderer.service.protocol.interfaces.base

import ge.wanderer.core.model.report.Report
import ge.wanderer.service.protocol.request.OperateOnContentRequest
import ge.wanderer.service.protocol.request.ReportContentRequest
import ge.wanderer.service.protocol.request.listing.ListingRequest
import ge.wanderer.service.protocol.response.ServiceListingResponse
import ge.wanderer.service.protocol.response.ServiceResponse

interface ReportableContentService {
    fun report(request: ReportContentRequest): ServiceResponse<String>
    fun listReportsForContent(contentId: Long, listingRequest: ListingRequest): ServiceListingResponse<Report>
}