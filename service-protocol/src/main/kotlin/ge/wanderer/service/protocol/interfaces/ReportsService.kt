package ge.wanderer.service.protocol.interfaces

import ge.wanderer.core.model.report.Report
import ge.wanderer.common.listing.ListingParams
import ge.wanderer.service.protocol.response.ServiceListingResponse
import ge.wanderer.service.protocol.response.ServiceResponse

interface ReportsService {

    fun list(params: ListingParams): ServiceListingResponse<Report>
    fun dismiss(reportId: Long): ServiceResponse<Report>
}