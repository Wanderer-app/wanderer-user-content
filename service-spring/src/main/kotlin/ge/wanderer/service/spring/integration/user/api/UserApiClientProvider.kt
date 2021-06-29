package ge.wanderer.service.spring.integration.user.api

import org.http4k.core.HttpHandler

interface UserApiClientProvider {
    fun getClient(): HttpHandler
}