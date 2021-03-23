package ge.wanderer.service.protocol.interfaces

import ge.wanderer.core.model.report.Report
import ge.wanderer.common.listing.ListingRequest
import ge.wanderer.service.protocol.response.ServiceListingResponse

interface ReportsService {

    fun list(request: ListingRequest): ServiceListingResponse<Report>
    fun dismiss(requestId: Long): ServiceListingResponse<Report>
}