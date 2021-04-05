package ge.wanderer.persistence.inMemory.repository

import ge.wanderer.core.model.report.Report
import ge.wanderer.persistence.repository.ReportRepository
import org.springframework.stereotype.Component
import java.util.concurrent.atomic.AtomicLong

@Component
class ReportRepositoryImpl: ReportRepository, BaseInMemoryRepository<Report>() {
    private val id = AtomicLong(1)

    override fun data(): HashMap<Long, Report> = hashMapOf()
    override fun nextId(): Long = id.getAndIncrement()
    override fun makePersistent(model: Report, id: Long): Report = model
}