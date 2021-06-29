package ge.wanderer.persistence.inMemory.filtering

import ge.wanderer.common.listing.FilterParam
import ge.wanderer.core.model.content.RateableContent
import ge.wanderer.core.model.content.UserAddedContent
import ge.wanderer.core.model.map.IPin
import org.joda.time.LocalDateTime

class FilterParameterEvaluator<T>(
    private val filterParam: FilterParam,
    private val element: T
) {
    fun evaluate(): Boolean {
        return checkFilter(filterParam, element)
    }

    private fun checkFilter(filterParam: FilterParam, element: T): Boolean = getFieldValue(element, filterParam).check()


    private fun getFieldValue(element: T, filterParam: FilterParam): Filter<*> {
        return when (element) {
            is IPin -> getFieldValueForPin(element, filterParam)
            is RateableContent -> getFieldValueForRateableContent(element, filterParam)
            is UserAddedContent -> getFieldValueForUserContent(element, filterParam)
            else -> error("Cant filter this model!")
        }
    }

    private fun <T : IPin> getFieldValueForPin(element: T, filterParam: FilterParam): Filter<*> {
        return when(filterParam.fieldName) {
            "pinType" -> stringFilter(element.type().toString(), filterParam)
            else -> getFieldValueForRateableContent(element, filterParam)
        }
    }

    private fun <T : RateableContent> getFieldValueForRateableContent(element: T, filterParam: FilterParam): Filter<*> {
        return when(filterParam.fieldName) {
            "rating" -> numberFilter(element.rating(), filterParam)
            else -> getFieldValueForUserContent(element, filterParam)
        }
    }

    private fun <T : UserAddedContent> getFieldValueForUserContent(element: T, filterParam: FilterParam): Filter<*> {
        return when(filterParam.fieldName) {
            "createdAt" -> timeFilter(element.createdAt(), filterParam)
            "creatorId" -> stringFilter(element.creator().id, filterParam)
            "contentType" -> stringFilter(element.contentType().toString(), filterParam)
            "isActive" -> booleanFilter(element.isActive(), filterParam)
            else -> error("Can't filter by field ${filterParam.fieldName}")
        }
    }

    private fun numberFilter(fieldValue: Number, filterParam: FilterParam): Filter<Number> =
        Filter(fieldValue, filterParam.compareValue, { field, compareValue -> checkNumber(field, compareValue, filterParam.operation)} )

    private fun timeFilter(fieldValue: LocalDateTime, filterParam: FilterParam): Filter<LocalDateTime> =
        Filter(fieldValue, filterParam.compareValue, { field, compareValue -> checkTime(field, compareValue, filterParam.operation) } )

    private fun stringFilter(fieldValue: String, filterParam: FilterParam): Filter<String> =
        Filter(fieldValue, filterParam.compareValue, { field, compareValue -> checkString(field, compareValue, filterParam.operation) } )

    private fun booleanFilter(fieldValue: Boolean, filterParam: FilterParam): Filter<Boolean> =
        Filter(fieldValue, filterParam.compareValue, { field, compareValue -> checkBool(field, compareValue, filterParam.operation) } )


    private data class Filter<T>(
        private val value: T,
        private val compareValue: String,
        private val function: (T, String) -> Boolean
    ) {
        fun check(): Boolean {
            return function.invoke(value, compareValue)
        }
    }
}