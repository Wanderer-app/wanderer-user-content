package ge.wanderer.service.spring.impl

import ge.wanderer.core.repository.CommentRepository
import ge.wanderer.core.repository.PostRepository
import ge.wanderer.service.spring.command.CommandProvider
import ge.wanderer.service.spring.configuration.ReportingConfigurationImpl
import ge.wanderer.service.spring.test_support.mockedUserService
import ge.wanderer.service.spring.test_support.testCommentPreviewProvider
import io.mockk.mockk
import org.junit.jupiter.api.Test
import kotlin.test.assertTrue

class PostServiceImplTest {

    private val userService = mockedUserService()
    private val postRepository = mockk<PostRepository>()
    private val commentRepository = mockk<CommentRepository>()
    private val service = PostServiceImpl(userService, testCommentPreviewProvider(), CommandProvider(), postRepository, commentRepository, ReportingConfigurationImpl())

    @Test
    fun aaa() {
        assertTrue(true)
    }
}