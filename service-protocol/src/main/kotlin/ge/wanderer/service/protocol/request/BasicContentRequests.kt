package ge.wanderer.service.protocol.request

import ge.wanderer.common.enums.ReportReason
import ge.wanderer.common.listing.ListingParams
import org.joda.time.LocalDateTime

data class OperateOnContentRequest (
    val contentId: Long,
    val userId: String,
    val date: LocalDateTime
)

data class ReportContentRequest (
    val contentId: Long,
    val userId: String,
    val date: LocalDateTime,
    val reportReason: ReportReason
)

data class ListCommentsRequest(
    val contentId: Long,
    val requestingUserId: String?,
    val listingParams: ListingParams
)