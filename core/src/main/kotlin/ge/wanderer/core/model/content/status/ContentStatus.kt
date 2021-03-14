package ge.wanderer.core.model.content.status

import org.joda.time.LocalDate


data class ContentStatus (
    val type: StatusType,
    val createdAt: LocalDate
)

enum class StatusType {
    ACTIVE, REMOVED, NOT_RELEVANT
}