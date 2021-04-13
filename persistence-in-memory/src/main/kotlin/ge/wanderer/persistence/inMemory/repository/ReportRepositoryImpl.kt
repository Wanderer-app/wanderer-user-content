package ge.wanderer.persistence.inMemory.repository

import ge.wanderer.core.model.report.Report
import ge.wanderer.persistence.inMemory.sorting.SequenceSorter
import ge.wanderer.persistence.repository.ReportRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.util.concurrent.atomic.AtomicLong

@Component
class ReportRepositoryImpl(
    @Autowired private val sorter: SequenceSorter<Report>
): ReportRepository, BaseInMemoryRepository<Report>(sorter) {
    private val id = AtomicLong(1)

    override fun data(): HashMap<Long, Report> = hashMapOf()
    override fun nextId(): Long = id.getAndIncrement()
    override fun makePersistent(model: Report, id: Long): Report = model

}