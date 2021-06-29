package ge.wanderer.service.spring.integration.user.api

import org.http4k.core.HttpHandler
import org.springframework.stereotype.Component

@Component
class MockedUserApiClientProvider: UserApiClientProvider {

    override fun getClient(): HttpHandler = mockedUserApiClient()
}