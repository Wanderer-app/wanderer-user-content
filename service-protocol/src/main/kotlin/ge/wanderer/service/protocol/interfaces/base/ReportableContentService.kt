package ge.wanderer.service.protocol.interfaces.base

import ge.wanderer.service.protocol.data.ReportData
import ge.wanderer.service.protocol.request.ReportContentRequest
import ge.wanderer.service.protocol.response.ServiceListingResponse
import ge.wanderer.service.protocol.response.ServiceResponse

interface ReportableContentService {
    fun report(request: ReportContentRequest): ServiceResponse<ReportData>
    fun listReportsForContent(contentId: Long): ServiceListingResponse<ReportData>
}