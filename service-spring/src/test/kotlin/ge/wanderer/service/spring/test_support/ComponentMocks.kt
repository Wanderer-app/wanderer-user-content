package ge.wanderer.service.spring.test_support

import ge.wanderer.common.enums.UserContentType
import ge.wanderer.core.integration.user.UserService
import ge.wanderer.core.model.comment.IComment
import ge.wanderer.core.model.map.IPin
import ge.wanderer.persistence.repository.CommentRepository
import ge.wanderer.persistence.repository.PinRepository
import ge.wanderer.service.spring.CommentPreviewProvider
import ge.wanderer.service.spring.configuration.ReportingConfigurationImpl
import io.mockk.every
import io.mockk.mockk

fun mockedUserService() = mockk<UserService> {
    every { findUserById(1) } returns jambura()
    every { findUserById(2) } returns patata()
    every { findUserById(3) } returns jangula()
    every { findUserById(4) } returns vipiSoxumski()
    every { findUserById(5) } returns kalduna()
    every { notifyContentStatusChange(any()) } returns Unit
    every { notifyAdministrationAboutReport(any()) } returns Unit
    every { getAdministrationUser() } returns jambura()
    every { usersContentWasRated(any(), any()) } returns Unit
    every { notifyContentWasCommented(any(), any()) } returns Unit
}

fun mockedCommentRepository(comments: List<IComment>) = mockk<CommentRepository> {
    every { findById(1) } returns comments[0]
    every { findById(2) } returns comments[1]
    every { findById(3) } returns comments[2]
    every { findById(4) } returns comments[3]
}

fun mockedPinRepository(pins: List<IPin>) = mockk<PinRepository> {
    every { findById(1) } returns pins[0]
    every { findById(2) } returns pins[1]
    every { findById(3) } returns pins[2]
    every { findById(4) } returns pins[3]
    every { findById(5) } returns pins[4]
    every { list(any()) } returns pins
}

fun testCommentPreviewProvider() =
    CommentPreviewProvider(mapOf(
        Pair(UserContentType.PIN, 3),
        Pair(UserContentType.POST, 3),
        Pair(UserContentType.POLL, 3),
        Pair(UserContentType.COMMENT, 3)
    ))

fun testReportingConfiguration() = ReportingConfigurationImpl(
    mapOf(
        Pair(UserContentType.PIN, 100),
        Pair(UserContentType.POST, 100),
        Pair(UserContentType.COMMENT, 100)
    ),
    mapOf(
        Pair(UserContentType.PIN, 100),
        Pair(UserContentType.POST, 100),
        Pair(UserContentType.COMMENT, 100)
    ),
    100
)

fun mockedReportingConfiguration() = mockk<ReportingConfigurationImpl> {
    every { shouldBeMarkedIrrelevant(any()) } returns false
    every { shouldBeRemoved(any()) } returns false
    every { shouldNotifyAdministration(any()) } returns false
}