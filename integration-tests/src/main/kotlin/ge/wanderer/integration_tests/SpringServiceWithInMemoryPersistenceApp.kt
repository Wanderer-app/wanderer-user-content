package ge.wanderer.integration_tests

import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication(scanBasePackages = ["ge.wanderer.service.spring", "ge.wanderer.persistence.inMemory"])
class SpringServiceWithInMemoryPersistenceApp
