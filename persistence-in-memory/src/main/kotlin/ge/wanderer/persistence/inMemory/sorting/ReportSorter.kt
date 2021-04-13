package ge.wanderer.persistence.inMemory.sorting

import ge.wanderer.common.listing.SortingParams
import ge.wanderer.core.model.report.Report
import org.springframework.stereotype.Component

@Component
class ReportSorter: SequenceSorter<Report> {
    override fun sort(sequence: Sequence<Report>, sortingParams: SortingParams): Sequence<Report> =
        when(sortingParams.fieldName) {
            "reportTime" -> sequence.sortByReportTime(sortingParams.sortingDirection)
            else -> error("Cant sort by ${sortingParams.fieldName}")
        }
}