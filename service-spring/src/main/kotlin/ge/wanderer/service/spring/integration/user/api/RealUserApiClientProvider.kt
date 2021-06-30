package ge.wanderer.service.spring.integration.user.api

import org.http4k.client.ApacheClient
import org.http4k.core.HttpHandler
import org.http4k.core.Uri
import org.http4k.core.then
import org.http4k.filter.ClientFilters
import org.http4k.filter.DebuggingFilters
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class RealUserApiClientProvider(
    @Autowired private val userApiUrl: String
): UserApiClientProvider {

    override fun getClient(): HttpHandler = realUserApiClient(userApiUrl)

    private fun realUserApiClient(apiBaseUri: String): HttpHandler =
        DebuggingFilters.PrintResponse()
            .then(
                ClientFilters.SetBaseUriFrom(Uri.of(apiBaseUri))
                    .then(ApacheClient()))

}