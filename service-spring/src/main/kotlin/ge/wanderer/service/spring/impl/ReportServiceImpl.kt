package ge.wanderer.service.spring.impl

import ge.wanderer.common.listing.ListingParams
import ge.wanderer.persistence.repository.ReportRepository
import ge.wanderer.service.protocol.data.ReportData
import ge.wanderer.service.protocol.interfaces.ReportsService
import ge.wanderer.service.protocol.response.ServiceListingResponse
import ge.wanderer.service.protocol.response.ServiceResponse
import ge.wanderer.service.spring.data.data
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class ReportServiceImpl(
    @Autowired private val reportRepository: ReportRepository
): ReportsService {

    override fun list(params: ListingParams): ServiceListingResponse<ReportData> {
        val reports = reportRepository.list(params)
        return ServiceListingResponse(true, "Reports fetched!", reports.size, params.batchNumber, reports.map { it.data() })
    }

    override fun dismiss(reportId: Long): ServiceResponse<ReportData> {
        reportRepository.delete(reportId)
        return ServiceResponse(true, "Report deleted!", null)
    }
}