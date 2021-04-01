package ge.wanderer.common

import com.fasterxml.jackson.databind.ObjectMapper

fun <T> toJson(obj: T) = ObjectMapper().writeValueAsString(obj)

inline fun <reified T> fromJson(jsonString: String): T = ObjectMapper().readValue(jsonString, T::class.java)