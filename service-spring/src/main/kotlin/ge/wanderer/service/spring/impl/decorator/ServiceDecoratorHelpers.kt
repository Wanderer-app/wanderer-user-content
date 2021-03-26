package ge.wanderer.service.spring.impl.decorator

import ge.wanderer.common.functions.asStandardMessage
import ge.wanderer.service.protocol.response.ServiceListingResponse
import ge.wanderer.service.protocol.response.ServiceResponse
import ge.wanderer.service.spring.data.noDataResponse
import java.lang.Exception

fun <T> handle(function: () -> ServiceResponse<T>): ServiceResponse<T> =
    try {
        function.invoke()
    } catch (e: Exception) {
        noDataResponse(false, e.asStandardMessage())
    }

fun <T> handleListing(function: () -> ServiceListingResponse<T>): ServiceListingResponse<T> =
    try {
        function.invoke()
    } catch (e: Exception) {
        ServiceListingResponse(false, e.asStandardMessage(), 0, 0, listOf())
    }