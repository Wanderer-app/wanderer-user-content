package ge.wanderer.integration_tests

import ge.wanderer.persistence.inMemory.WandererInMemoryPersistenceApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication(scanBasePackages = ["ge.wanderer.service.spring", "ge.wanderer.persistence.inMemory"])
class SpringServiceWithInMemoryPersistenceApp

fun main(args: Array<String>) {
    runApplication<WandererInMemoryPersistenceApplication>(*args)
}