package ge.wanderer.service.protocol.interfaces

import ge.wanderer.common.listing.ListingParams
import ge.wanderer.service.protocol.data.ReportData
import ge.wanderer.service.protocol.response.ServiceListingResponse
import ge.wanderer.service.protocol.response.ServiceResponse

interface ReportsService {

    fun list(params: ListingParams): ServiceListingResponse<ReportData>
    fun dismiss(reportId: Long): ServiceResponse<ReportData>
}