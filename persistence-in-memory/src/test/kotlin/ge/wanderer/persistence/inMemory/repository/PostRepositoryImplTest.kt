package ge.wanderer.persistence.inMemory.repository

import ge.wanderer.common.now
import ge.wanderer.core.repository.TRANSIENT_ID
import ge.wanderer.persistence.inMemory.WandererInMemoryPersistenceApplication
import ge.wanderer.persistence.inMemory.support.createNewPostWithoutFiles
import ge.wanderer.persistence.inMemory.support.jambura
import io.mockk.mockk
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.lang.IllegalStateException
import kotlin.test.assertEquals

@SpringBootTest(classes = [WandererInMemoryPersistenceApplication::class])
class PostRepositoryImplTest(
    @Autowired private val postRepositoryImpl: PostRepositoryImpl
) {

    @Test
    fun listsPosts() {
        val posts = postRepositoryImpl.list(mockk())
        assertEquals(5, posts.size)
    }

    @Test
    fun persistsPost() {
        val post = createNewPostWithoutFiles(TRANSIENT_ID, jambura(), "aaa", now())
        postRepositoryImpl.persist(post)

        val persistedPost = postRepositoryImpl.findById(6)
        assertEquals(jambura(), persistedPost.creator())
        assertEquals("aaa", persistedPost.content())
    }

    @Test
    fun deletesPost() {
        postRepositoryImpl.delete(1)
        assertThrows<IllegalStateException> { postRepositoryImpl.findById(1) }
    }
}