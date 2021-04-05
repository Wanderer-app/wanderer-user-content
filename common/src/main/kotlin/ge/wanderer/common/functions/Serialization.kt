package ge.wanderer.common

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule

fun <T> toJson(obj: T) = ObjectMapper().writeValueAsString(obj)

inline fun <reified T> fromJson(jsonString: String): T =
    ObjectMapper()
        .registerModule(KotlinModule())
        .readValue(jsonString, T::class.java)