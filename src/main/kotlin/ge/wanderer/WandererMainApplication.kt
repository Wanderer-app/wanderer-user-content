package ge.wanderer

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication(scanBasePackages = ["ge.wanderer.*"])
class WandererMainApplication

fun main(args: Array<String>) {
    runApplication<WandererMainApplication>(*args)
}