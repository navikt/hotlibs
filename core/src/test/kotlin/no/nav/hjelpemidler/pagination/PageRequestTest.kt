package no.nav.hjelpemidler.pagination

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import kotlin.test.Test

class PageRequestTest {
    @Test
    fun `Ugyldig PageRequest`() {
        shouldThrow<IllegalArgumentException> { PageRequest(pageNumber = 0) }
        shouldThrow<IllegalArgumentException> { PageRequest(pageSize = 0) }
    }

    @Test
    fun `Limit og offset`() {
        PageRequest(pageNumber = 1).offset shouldBe 0
        PageRequest(pageNumber = 2, pageSize = 20).offset shouldBe 20
        PageRequest(pageNumber = 3, pageSize = 30).offset shouldBe 60
    }
}
