package ge.wanderer.service.spring.configuration

import ge.wanderer.common.enums.ReportReason.IRRELEVANT
import ge.wanderer.common.enums.UserContentType
import ge.wanderer.core.configuration.ReportingConfiguration
import ge.wanderer.core.model.content.ReportableContent
import ge.wanderer.core.model.map.IPin
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class ReportingConfigurationImpl(
    @Autowired private val reportedContentNotificationProperties: Map<UserContentType, Int>,
    @Autowired private val reportedContentRemovalProperties: Map<UserContentType, Int>,
    @Value("\${pin.reporting.reports-number-to-mark-irrelevant:10}") private val maxIrrelevantReportsForPin: Int
): ReportingConfiguration {

    override fun shouldBeRemoved(reportableContent: ReportableContent): Boolean =
        reportableContent.isActive() &&
                reportableContent.reports().filter { it.reason != IRRELEVANT }.size >= reportsNumberToNotify(reportableContent)


    override fun shouldNotifyAdministration(reportableContent: ReportableContent): Boolean {
        return reportableContent.reports().size >= reportsNumberToRemove(reportableContent)
    }

    override fun shouldBeMarkedIrrelevant(pin: IPin): Boolean =
        pin.reports()
            .filter { it.reason == IRRELEVANT }
            .size >= maxIrrelevantReportsForPin

    private fun reportsNumberToNotify(reportableContent: ReportableContent) =
        reportedContentRemovalProperties[reportableContent.contentType()]
        ?: error("Reporting configuration does not support ${reportableContent.contentType()}")

    private fun reportsNumberToRemove(reportableContent: ReportableContent) =
        reportedContentNotificationProperties[reportableContent.contentType()]
        ?: error("Reporting configuration does not support ${reportableContent.contentType()}")

}