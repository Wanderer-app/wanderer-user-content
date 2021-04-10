package ge.wanderer.web.api.spring.configuration

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.PropertySource
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer


@Configuration
@PropertySource("classpath:web-api.properties")
class CorsConfiguration {

    @Bean
    fun corsConfigurer(
        @Value("\${allowed-api-clients}") apiClients: Array<String>
    ): WebMvcConfigurer =
        object : WebMvcConfigurer {
            override fun addCorsMappings(registry: CorsRegistry) {
                registry.addMapping("/api/**").allowedOrigins(*apiClients)
            }
        }

}