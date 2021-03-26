package ge.wanderer.service.spring.test_support

import ge.wanderer.core.integration.user.UserService
import ge.wanderer.core.model.comment.IComment
import ge.wanderer.core.model.map.IPin
import ge.wanderer.core.repository.CommentRepository
import ge.wanderer.core.repository.PinRepository
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
}