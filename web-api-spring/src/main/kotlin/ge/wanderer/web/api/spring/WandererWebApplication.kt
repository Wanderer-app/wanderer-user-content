package ge.wanderer.web.api.spring

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class WandererWebApplication

fun main(args: Array<String>) {
    runApplication<WandererWebApplication>(*args)
}