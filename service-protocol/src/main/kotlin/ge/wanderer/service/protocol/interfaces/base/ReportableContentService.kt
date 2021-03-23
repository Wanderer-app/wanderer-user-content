package ge.wanderer.service.protocol.interfaces.base

import ge.wanderer.core.model.report.Report
import ge.wanderer.service.protocol.request.ReportContentRequest
import ge.wanderer.service.protocol.response.NoDataResponse
import ge.wanderer.service.protocol.response.ServiceListingResponse

interface ReportableContentService {
    fun report(request: ReportContentRequest): NoDataResponse
    fun listReportsForContent(contentId: Long): ServiceListingResponse<Report>
}