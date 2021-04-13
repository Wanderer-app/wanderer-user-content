package ge.wanderer.persistence.inMemory.sorting

import ge.wanderer.common.listing.SortingDirection
import ge.wanderer.core.model.content.RateableContent
import ge.wanderer.core.model.content.UserAddedContent
import ge.wanderer.core.model.report.Report

fun <T: RateableContent> Sequence<T>.sortByRating(sortingDirection: SortingDirection): Sequence<T> =
    if (sortingDirection == SortingDirection.DESCENDING) {
        this.sortedByDescending { it.rating() }
    } else {
        this.sortedBy { it.rating() }
    }

fun <T: UserAddedContent> Sequence<T>.sortByCreateTime(sortingDirection: SortingDirection): Sequence<T> =
    if (sortingDirection == SortingDirection.DESCENDING) {
        this.sortedByDescending { it.createdAt() }
    } else {
        this.sortedBy { it.createdAt() }
    }

fun Sequence<Report>.sortByReportTime(sortingDirection: SortingDirection): Sequence<Report> =
    if (sortingDirection == SortingDirection.DESCENDING) {
        this.sortedByDescending { it.reportTime }
    } else {
        this.sortedBy { it.reportTime }
    }