package ge.wanderer.core.model.content

import ge.wanderer.core.model.report.Report

interface ReportableContent: UserAddedContent {
    fun report(report: Report)
    fun reports(): Set<Report>
}