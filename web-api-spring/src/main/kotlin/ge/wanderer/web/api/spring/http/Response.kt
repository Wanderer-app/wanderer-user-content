package ge.wanderer.web.api.spring.http

import ge.wanderer.service.protocol.response.ServiceListingResponse
import ge.wanderer.service.protocol.response.ServiceResponse
import org.springframework.http.ResponseEntity

fun <T> httpResponse(serviceResponse: ServiceResponse<T>): ResponseEntity<ServiceResponse<T>> =
    if (serviceResponse.isSuccessful) {
        ResponseEntity.ok(serviceResponse)
    } else {
        ResponseEntity.badRequest().body(serviceResponse)
    }

fun <T> httpResponse(serviceListingResponse: ServiceListingResponse<T>): ResponseEntity<ServiceListingResponse<T>> =
    if (serviceListingResponse.isSuccessful) {
        ResponseEntity.ok(serviceListingResponse)
    } else {
        ResponseEntity.badRequest().body(serviceListingResponse)
    }