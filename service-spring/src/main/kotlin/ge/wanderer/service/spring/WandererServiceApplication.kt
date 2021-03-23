package ge.wanderer.service.spring

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class WandererServiceApplication

fun main(args: Array<String>) {
    runApplication<WandererServiceApplication>(*args)
}