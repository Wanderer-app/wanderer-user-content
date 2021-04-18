package ge.wanderer.persistence.inMemory.filtering

import ge.wanderer.common.dateTime
import ge.wanderer.common.listing.FilterOperation.*
import ge.wanderer.common.listing.FilterParam
import ge.wanderer.persistence.inMemory.support.createNewPostWithoutFiles
import ge.wanderer.persistence.inMemory.support.jambura
import ge.wanderer.persistence.inMemory.support.patata
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.lang.IllegalStateException
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class FilterParameterEvaluatorTest {
    @Test
    fun correctlyChecksNumberParameters() {
        val post = createNewPostWithoutFiles(1, jambura(), "zd all" , dateTime("2020-04-15T12:12:11"))

        assertTrue(evaluate(FilterParam("creatorId", IS, "1"), post))
        assertFalse(evaluate(FilterParam("creatorId", IS_NOT, "1"), post))
        assertFalse(evaluate(FilterParam("creatorId", IS_MORE_THEN, "2"), post))
        assertFalse(evaluate(FilterParam("creatorId", IS_LESS_THEN, "-2"), post))
        assertTrue(evaluate(FilterParam("creatorId", IS_MORE_THEN, "-2"), post))
        assertTrue(evaluate(FilterParam("creatorId", IS_LESS_THEN, "2"), post))
    }

        @Test
    fun correctlyChecksStringParameters() {
        val post = createNewPostWithoutFiles(1, jambura(), "zd all" , dateTime("2020-04-15T12:12:11"))

        assertTrue(evaluate(FilterParam("contentType", IS, "POST"), post))
        assertFalse(evaluate(FilterParam("contentType", IS_NOT, "POST"), post))
        assertFalse(evaluate(FilterParam("contentType", IS, "PIN"), post))
        assertThrows<IllegalStateException> { evaluate(FilterParam("contentType", IS_MORE_THEN, "PIN"), post) }
        assertThrows<IllegalStateException> { evaluate(FilterParam("contentType", IS_LESS_THEN, "PIN"), post) }
    }

    @Test
    fun correctlyChecksDateParameters() {
        val post = createNewPostWithoutFiles(1, jambura(), "zd all", dateTime("2020-04-15T12:12:11"))

        assertTrue(evaluate(FilterParam("createdAt", IS, "2020-04-15T12:12:11"), post))
        assertTrue(evaluate(FilterParam("createdAt", IS_NOT, "2020-04-15T12:12:12"), post))
        assertTrue(evaluate(FilterParam("createdAt", IS_MORE_THEN, "2020-04-15T12:12:10"), post))
        assertTrue(evaluate(FilterParam("createdAt", IS_LESS_THEN, "2020-04-15T12:12:12"), post))
        assertFalse(evaluate(FilterParam("createdAt", IS_MORE_THEN, "2020-04-15T12:12:12"), post))
        assertFalse(evaluate(FilterParam("createdAt", IS_LESS_THEN, "2020-04-15T12:12:10"), post))
    }

    @Test
    fun correctlyChecksBooleanParameters() {
        val post = createNewPostWithoutFiles(1, jambura(), "zd all", dateTime("2020-04-15T12:12:11"))

        assertTrue(evaluate(FilterParam("isActive", IS, "true"), post))
        assertTrue(evaluate(FilterParam("isActive", IS_NOT, "false"), post))
        assertFalse(evaluate(FilterParam("isActive", IS, "false"), post))
        assertThrows<IllegalStateException> { evaluate(FilterParam("isActive", IS_MORE_THEN, "false"), post) }
        assertThrows<IllegalStateException> { evaluate(FilterParam("isActive", IS_LESS_THEN, "false"), post) }
    }

    @Test
    fun failsIfElementTypeIsNotSupported() {
        val exception = assertThrows<IllegalStateException> {
            evaluate(FilterParam("contentType", IS_LESS_THEN, "PIN"), jambura())
        }
        assertEquals("Cant filter this model!", exception.message!!)

    }

    @Test
    fun failsIfFieldNameIsNotSupported() {
        val post = createNewPostWithoutFiles(1, jambura(), "zd all", dateTime("2020-04-15T12:12:11"))

        val exception = assertThrows<IllegalStateException> {
            evaluate(FilterParam("jandaba", IS, "jandaba"), post)
        }
        assertEquals("Can't filter by field jandaba", exception.message!!)
    }

    @Test
    fun worksForCollection() {
        val posts = listOf(
            createNewPostWithoutFiles(1, jambura(), "aaa", dateTime("2020-04-15T12:12:10")),
            createNewPostWithoutFiles(2, patata(), "ccc", dateTime("2020-04-15T12:12:20")),
            createNewPostWithoutFiles(3, jambura(), "bbb", dateTime("2020-04-15T12:12:15"))
        )

        val jamburasPosts = posts.filter { evaluate(FilterParam("creatorId", IS, jambura().id.toString()), it) }
        assertEquals(2, jamburasPosts.size)

        val patatasPosts = posts.filter { evaluate(FilterParam("creatorId", IS, patata().id.toString()), it) }
        assertEquals(1, patatasPosts.size)
        assertEquals(2, patatasPosts.first().id())

        val filteredByDate = posts.filter { evaluate(FilterParam("createdAt", IS_MORE_THEN, "2020-04-15T12:12:14"), it) }
        assertEquals(2, filteredByDate.size)

    }

    private fun <T> evaluate(filterParam: FilterParam, element: T): Boolean = FilterParameterEvaluator(filterParam, element).evaluate()

}