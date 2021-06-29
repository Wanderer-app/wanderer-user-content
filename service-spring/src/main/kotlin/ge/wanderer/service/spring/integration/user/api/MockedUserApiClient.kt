package ge.wanderer.service.spring.integration.user.api

import ge.wanderer.common.functions.toJson
import ge.wanderer.service.spring.integration.user.api.response.GetUserResponse
import org.http4k.core.*
import org.http4k.filter.DebuggingFilters
import org.http4k.lens.Path
import org.http4k.routing.RoutingHttpHandler
import org.http4k.routing.bind
import org.http4k.routing.routes

fun mockedUserApiClient(): HttpHandler = RequireOkResponse().then(DebuggingFilters.PrintResponse().then(routes(
    getById(), getAdmin(), notify()
)))

private fun getById(): RoutingHttpHandler =
    "/get_user" bind Method.GET to { request: Request ->
        val id = request.query("id")!!

        val user = when(id) {
            "85fa0681-b7bd-4ee3-b5b5-eb2672181ae2" -> GetUserResponse(id, "Nika", "Patatishvili", "aaa", 0)
            "5760b116-6aab-4f04-b8be-650e27a85d09" -> GetUserResponse(id, "Nika", "Jamburia", "aaa", 1)
            else -> GetUserResponse(id, "vigaca", "vigaca", "aaa", 0)
        }

        if (id.isBlank()) {
            Response(Status.BAD_REQUEST).body("Id should be provided")
        }
        else if(id.length < 5) {
            Response(Status.NOT_FOUND).body("User not found")
        }
        else {
            Response(Status.OK).body(toJson(user))
        }


    }

private fun getAdmin(): RoutingHttpHandler =
    "/get_administration_user" bind Method.GET to { request: Request ->
        Response(Status.OK)
            .body(toJson(GetUserResponse("1", "Nika", "Jamburia", "aaa", 1)))
    }

private fun notify(): RoutingHttpHandler =
    "/notifications" bind Method.POST to { request: Request ->
        Response(Status.OK)
            .body("Notification Sent")
    }