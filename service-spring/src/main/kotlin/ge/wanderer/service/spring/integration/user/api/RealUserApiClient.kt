package ge.wanderer.service.spring.integration.user.api

import org.http4k.client.JavaHttpClient
import org.http4k.core.HttpHandler
import org.http4k.core.Uri
import org.http4k.core.then
import org.http4k.filter.ClientFilters
import org.http4k.filter.ClientFilters.SetBaseUriFrom
import org.http4k.filter.DebuggingFilters
import org.http4k.filter.DebuggingFilters.PrintResponse

fun realUserApiClient(apiBaseUri: String): HttpHandler =
    PrintResponse()
        .then(SetBaseUriFrom(Uri.of(apiBaseUri))
            .then(JavaHttpClient()))

