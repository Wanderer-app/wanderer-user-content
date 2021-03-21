package ge.wanderer.core.command.content

import ge.wanderer.core.command.Command
import ge.wanderer.core.command.CommandExecutionResult
import ge.wanderer.core.command.success
import ge.wanderer.core.model.content.ReportableContent
import ge.wanderer.core.model.report.Report

abstract class BaseReportContentCommand<T: ReportableContent>(
    private val content: T
): Command<T> {
    override fun execute(): CommandExecutionResult<T> {
        content.report(createReport())
        return success("Content Reported!", afterReport(content))
    }

    abstract fun createReport(): Report
    abstract fun afterReport(reportedContent: T): T
}