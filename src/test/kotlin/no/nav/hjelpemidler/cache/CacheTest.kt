package no.nav.hjelpemidler.cache

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import no.nav.hjelpemidler.http.test.shouldBe
import kotlin.test.BeforeTest
import kotlin.test.Test

class CacheTest {
    private val key = "test"
    private val loader = mockk<(String) -> String>()
    private val cache = createLoadingCache(loader = loader)

    @BeforeTest
    fun setUp() {
        every {
            loader(key)
        } returns "new"
    }

    @Test
    fun `oppdaterer cache hvis ingen verdi fra før`() {
        val value = cache.get(key)
        value shouldBe "new"
        verify(exactly = 1) {
            loader(key)
        }
    }

    @Test
    fun `oppdaterer ikke cache hvis verdi fra før`() {
        cache.put(key, "old")
        val value = cache.get(key)
        value shouldBe "old"
        verify(exactly = 0) {
            loader(key)
        }
    }
}
