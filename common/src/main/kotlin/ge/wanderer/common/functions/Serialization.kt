package ge.wanderer.common

import com.fasterxml.jackson.databind.ObjectMapper

fun <T> toJson(obj: T) = ObjectMapper().writeValueAsString(obj)