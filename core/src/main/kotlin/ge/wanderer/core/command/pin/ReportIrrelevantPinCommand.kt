package ge.wanderer.core.command.pin

import ge.wanderer.common.constants.TRANSIENT_ID
import ge.wanderer.common.enums.ReportReason
import ge.wanderer.core.command.content.BaseReportContentCommand
import ge.wanderer.core.configuration.ReportingConfiguration
import ge.wanderer.core.integration.user.User
import ge.wanderer.core.integration.user.UserService
import ge.wanderer.core.model.map.IPin
import ge.wanderer.core.model.report.Report
import org.joda.time.LocalDateTime

class ReportIrrelevantPinCommand(
    private val reporter: User,
    private val reportTime: LocalDateTime,
    pin: IPin,
    private val userService: UserService,
    private val reportingConfiguration: ReportingConfiguration
): BaseReportContentCommand<IPin>(pin) {

    override fun createReport(): Report = Report(TRANSIENT_ID, reporter, reportTime, ReportReason.IRRELEVANT)

    override fun afterReport(reportedContent: IPin): IPin =
        if (reportingConfiguration.shouldBeMarkedIrrelevant(reportedContent)) {
            MarkPinAsIrrelevantCommand(reportedContent, reportTime, userService).execute().returnedModel
        } else {
            reportedContent
        }

}