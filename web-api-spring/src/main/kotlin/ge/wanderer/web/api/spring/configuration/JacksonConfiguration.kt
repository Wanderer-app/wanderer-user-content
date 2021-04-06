package ge.wanderer.web.api.spring.configuration

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.joda.JodaModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder

@Configuration
class JacksonConfiguration {

    @Bean
    @Primary
    fun getObjectMapper(builder: Jackson2ObjectMapperBuilder): ObjectMapper {
        val mapper = builder.build<ObjectMapper>()
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
        mapper.registerModules(KotlinModule(), JodaModule())
        return mapper
    }
}