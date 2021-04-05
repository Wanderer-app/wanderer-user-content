package ge.wanderer.core.command.content

import ge.wanderer.common.constants.TRANSIENT_ID
import ge.wanderer.common.enums.ReportReason
import ge.wanderer.core.configuration.ReportingConfiguration
import ge.wanderer.core.integration.user.User
import ge.wanderer.core.integration.user.UserService
import ge.wanderer.core.model.content.ReportableContent
import ge.wanderer.core.model.report.Report
import org.joda.time.LocalDateTime

class ReportContentCommand(
    private val reporter: User,
    private val reportTime: LocalDateTime,
    private val reportReason: ReportReason,
    reportableContent: ReportableContent,
    private val userService: UserService,
    private val reportingConfiguration: ReportingConfiguration
): BaseReportContentCommand<ReportableContent>(reportableContent) {

    override fun createReport(): Report = Report(TRANSIENT_ID, reporter, reportTime, reportReason)

    override fun afterReport(reportedContent: ReportableContent): ReportableContent {
        if (reportingConfiguration.shouldBeRemoved(reportedContent)) {
            remove(reportedContent)
        }
        if (reportingConfiguration.shouldNotifyAdministration(reportedContent)) {
            userService.notifyAdministrationAboutReport(reportedContent)
        }

        return reportedContent
    }

    private fun remove(content: ReportableContent) =
        RemoveContentCommand(
            userService.getAdministrationUser(),
            content,
            reportTime,
            userService
        ).execute()
}