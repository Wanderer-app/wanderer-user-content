package ge.wanderer.service.spring.integration.user.api.response

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class GetUserResponse(
    val _id: String,
    val name: String,
    val surname: String,
    val username: String,
    val privilege: Int
)