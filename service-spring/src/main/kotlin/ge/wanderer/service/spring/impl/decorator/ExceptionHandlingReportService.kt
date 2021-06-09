package ge.wanderer.service.spring.impl.decorator

import ge.wanderer.common.listing.ListingParams
import ge.wanderer.service.protocol.data.ReportData
import ge.wanderer.service.protocol.interfaces.ReportsService
import ge.wanderer.service.protocol.response.ServiceListingResponse
import ge.wanderer.service.protocol.response.ServiceResponse
import ge.wanderer.service.spring.impl.ReportServiceImpl
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Primary
import org.springframework.stereotype.Component

@Primary
@Component
class ExceptionHandlingReportService(
    @Autowired private val reportServiceImpl: ReportServiceImpl
): ReportsService {

    override fun list(params: ListingParams): ServiceListingResponse<ReportData> =
        handleListing { reportServiceImpl.list(params) }

    override fun dismiss(reportId: Long): ServiceResponse<ReportData> =
        handle { reportServiceImpl.dismiss(reportId) }
}