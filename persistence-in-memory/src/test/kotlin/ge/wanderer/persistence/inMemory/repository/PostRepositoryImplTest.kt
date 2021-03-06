package ge.wanderer.persistence.inMemory.repository

import ge.wanderer.common.constants.TRANSIENT_ID
import ge.wanderer.common.now
import ge.wanderer.persistence.inMemory.WandererInMemoryPersistenceApplication
import ge.wanderer.persistence.inMemory.support.DEFAULT_LISTING_PARAMS
import ge.wanderer.persistence.inMemory.support.createNewPostWithoutFiles
import ge.wanderer.persistence.inMemory.support.jambura
import io.mockk.mockk
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@SpringBootTest(classes = [WandererInMemoryPersistenceApplication::class])
class PostRepositoryImplTest(
    @Autowired private val postRepositoryImpl: PostRepositoryImpl
) {

    @Test
    fun listsPosts() {
        val posts = postRepositoryImpl.list(DEFAULT_LISTING_PARAMS)
        assertTrue(posts.isNotEmpty())
    }

    @Test
    fun persistsPost() {
        val post = createNewPostWithoutFiles(TRANSIENT_ID, jambura(), "aaa", now())
        val newPostId = postRepositoryImpl.persist(post).id()

        val persistedPost = postRepositoryImpl.findById(6)
        assertEquals(jambura(), persistedPost.creator())
        assertEquals("aaa", persistedPost.content())
        assertEquals(newPostId, postRepositoryImpl.findById(newPostId).id())
    }

    @Test
    fun deletesPost() {
        postRepositoryImpl.delete(1)
        assertThrows<IllegalStateException> { postRepositoryImpl.findById(1) }
    }
}