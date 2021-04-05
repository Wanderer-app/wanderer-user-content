package ge.wanderer.service.spring.impl

import ge.wanderer.core.model.report.Report
import ge.wanderer.persistence.listing.ListingParams
import ge.wanderer.persistence.repository.ReportRepository
import ge.wanderer.service.protocol.interfaces.ReportsService
import ge.wanderer.service.protocol.response.ServiceListingResponse
import ge.wanderer.service.protocol.response.ServiceResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class ReportServiceImpl(
    @Autowired private val reportRepository: ReportRepository
): ReportsService {

    override fun list(params: ListingParams): ServiceListingResponse<Report> {
        val reports = reportRepository.list(params)
        return ServiceListingResponse(true, "Reports fetched!", reports.size, params.batchNumber, reports)
    }

    override fun dismiss(reportId: Long): ServiceResponse<Report> {
        reportRepository.delete(reportId)
        return ServiceResponse(true, "Report deleted!", null)
    }
}