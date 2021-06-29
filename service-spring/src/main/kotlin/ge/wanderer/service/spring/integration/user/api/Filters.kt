package ge.wanderer.service.spring.integration.user.api

import org.http4k.core.Filter
import org.http4k.core.HttpHandler
import org.http4k.core.Request

class RequireOkResponse: Filter {
    override fun invoke(next: HttpHandler): HttpHandler =
        { request: Request ->
            val response = next(request)
            if(response.status.code != 200) {
                error(response.status.code.toString() + " " + response.bodyString())
            }
            response
        }
}
