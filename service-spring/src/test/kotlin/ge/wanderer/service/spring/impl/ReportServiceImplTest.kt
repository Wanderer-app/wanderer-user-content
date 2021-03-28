package ge.wanderer.service.spring.impl

import ge.wanderer.common.listing.ListingParams
import ge.wanderer.common.now
import ge.wanderer.core.model.report.Report
import ge.wanderer.core.model.report.ReportReason.*
import ge.wanderer.core.repository.ReportRepository
import ge.wanderer.service.spring.test_support.jambura
import ge.wanderer.service.spring.test_support.jangula
import ge.wanderer.service.spring.test_support.patata
import ge.wanderer.service.spring.test_support.vipiSoxumski
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

class ReportServiceImplTest {

    private val reportRepository = mockk<ReportRepository>() { every { delete(any()) } returns Unit }
    private val service = ReportServiceImpl(reportRepository)

    @Test
    fun listsReports() {
        every { reportRepository.list(any()) } returns listOf(
            Report(1, jambura(), now(), OFFENSIVE_CONTENT),
            Report(2, jangula(), now(), INAPPROPRIATE_CONTENT),
            Report(3, patata(), now(), OFFENSIVE_CONTENT),
            Report(4, vipiSoxumski(), now(), INAPPROPRIATE_CONTENT)
        )

        val response = service.list(ListingParams(5, 1, null, listOf()))
        assertTrue(response.isSuccessful)
        assertEquals("Reports fetched!", response.message)
        assertEquals(4, response.resultSize)
        assertEquals(4, response.data.size)

    }

    @Test
    fun correctlyDismissesReport() {
        val response = service.dismiss(1)
        assertTrue(response.isSuccessful)
        assertEquals("Report deleted!", response.message)
        assertNull(response.data)
        verify(exactly = 1) { reportRepository.delete(1) }
    }
}