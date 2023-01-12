package no.nav.hjelpemidler.http.openid

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import no.nav.hjelpemidler.http.test.shouldBe
import kotlin.test.Test

class CacheTest {
    private val key = "test"
    private val predicate = mockk<CachePredicate<String, String>>()
    private val block = mockk<CacheUpdater<String, String>>()

    @Test
    fun `oppdaterer cache hvis ingen verdi fra før`() {
        val cache = Cache<String, String>()

        cache.size shouldBe 0
        cache[key] shouldBe null

        coEvery { predicate.invoke(key, any()) } returns true
        coEvery { block.invoke(key, null) } returns "newValue"

        val value = runBlocking {
            cache.putIf(key, predicate, block)
        }

        coVerify(exactly = 0) { predicate.invoke(any(), any()) }
        coVerify(exactly = 1) { block.invoke(any(), any()) }

        value shouldBe "newValue"
    }

    @Test
    fun `oppdaterer ikke cache hvis verdi fra før`() {
        val cache = Cache(mutableMapOf(key to "oldValue"))

        cache.size shouldBe 1
        cache[key] shouldBe "oldValue"

        coEvery { predicate.invoke(key, any()) } returns false
        coEvery { block.invoke(key, "oldValue") } returns "newValue"

        val value = runBlocking {
            cache.putIf(key, predicate, block)
        }

        coVerify(exactly = 1) { predicate.invoke(any(), any()) }
        coVerify(exactly = 0) { block.invoke(any(), any()) }

        value shouldBe "oldValue"
    }

    @Test
    fun `oppdaterer cache hvis verdi fra før`() {
        val cache = Cache(mutableMapOf(key to "oldValue"))

        cache.size shouldBe 1
        cache[key] shouldBe "oldValue"

        coEvery { predicate.invoke(key, any()) } returns true
        coEvery { block.invoke(key, "oldValue") } returns "newValue"

        val value = runBlocking {
            cache.putIf(key, predicate, block)
        }

        coVerify(exactly = 1) { predicate.invoke(any(), any()) }
        coVerify(exactly = 1) { block.invoke(any(), any()) }

        value shouldBe "newValue"
    }
}
