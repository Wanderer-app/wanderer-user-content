package ge.wanderer.persistence.inMemory.filtering

import ge.wanderer.common.dateTime
import ge.wanderer.common.listing.FilterOperation
import ge.wanderer.common.listing.FilterOperation.*
import org.joda.time.LocalDateTime

fun checkNumber(fieldValue: Number, compareValue: String, filterOperation: FilterOperation): Boolean {
    val field = fieldValue.toFloat()
    val compareValueFloat = compareValue.toFloat()

    return when(filterOperation) {
        IS -> field == compareValueFloat
        IS_NOT -> field != compareValueFloat
        IS_MORE_THEN -> field > compareValueFloat
        IS_LESS_THEN -> field < compareValueFloat
    }
}

fun checkString(fieldValue: String, compareValue: String, filterOperation: FilterOperation): Boolean {
    return when(filterOperation) {
        IS -> fieldValue == compareValue
        IS_NOT -> fieldValue != compareValue
        IS_MORE_THEN -> error("Operation $filterOperation not supported for string or bool")
        IS_LESS_THEN -> error("Operation $filterOperation not supported for string or bool")
    }
}

fun checkTime(fieldValue: LocalDateTime, compareValue: String, filterOperation: FilterOperation): Boolean {
    val compareValueTime = dateTime(compareValue)

    return when(filterOperation) {
        IS -> fieldValue == compareValueTime
        IS_NOT -> fieldValue != compareValueTime
        IS_MORE_THEN -> fieldValue.isAfter(compareValueTime)
        IS_LESS_THEN -> fieldValue.isBefore(compareValueTime)
    }
}

fun checkBool(fieldValue: Boolean, compareValue: String, filterOperation: FilterOperation): Boolean {
    val fieldValueString = if (fieldValue) { "true" } else { "false" }
    return checkString(fieldValueString, compareValue, filterOperation)
}

