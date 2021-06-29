package ge.wanderer.service.spring.integration.user.api

import org.http4k.core.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.lang.IllegalStateException
import kotlin.test.assertEquals

class RequireOkResponseTest {

    @Test
    fun doesNothingIfResponseIsOk() {

        val filteredServer = RequireOkResponse().then { request -> Response(Status.OK).body("OK") }

        val response = filteredServer(Request(Method.GET, ""))
        assertEquals(response, Response(Status.OK).body("OK"))
    }

    @Test
    fun throwsExceptionOnUnsuccessfullRequest() {

        val filteredServer = RequireOkResponse().then { request -> Response(Status.BAD_REQUEST).body("Bad request") }

        val exception = assertThrows<IllegalStateException> { filteredServer(Request(Method.GET, "")) }
        assertEquals("400 Bad request", exception.message!!)
    }
}