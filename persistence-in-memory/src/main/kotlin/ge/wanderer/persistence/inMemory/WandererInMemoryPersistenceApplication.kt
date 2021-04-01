package ge.wanderer.persistence.inMemory

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class WandererInMemoryPersistenceApplication

fun main(args: Array<String>) {
    runApplication<WandererInMemoryPersistenceApplication>(*args)
}