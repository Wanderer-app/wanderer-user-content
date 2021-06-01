package ge.wanderer.web.api.spring.http

import ge.wanderer.service.protocol.response.ServiceListingResponse
import ge.wanderer.service.protocol.response.ServiceResponse
import org.springframework.http.ResponseEntity

fun <T> httpResponse(serviceResponse: ServiceResponse<T>): ResponseEntity<ServiceResponse<T>> = ResponseEntity.ok(serviceResponse)
fun <T> httpResponse(serviceListingResponse: ServiceListingResponse<T>): ResponseEntity<ServiceListingResponse<T>> = ResponseEntity.ok(serviceListingResponse)
