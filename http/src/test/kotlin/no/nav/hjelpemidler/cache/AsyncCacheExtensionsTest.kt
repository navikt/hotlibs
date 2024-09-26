package no.nav.hjelpemidler.cache

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.test.runTest
import no.nav.hjelpemidler.time.nå
import java.time.Instant
import kotlin.test.Test

class AsyncCacheExtensionsTest {
    private val cache = createCache().buildAsync<String, Instant>()

    @Test
    fun `Skal kaste feil fra getAsync`() = runTest {
        shouldThrow<IllegalStateException> {
            cache.getAsync("test") { error("Error!") }
        }
    }

    @Test
    fun `Skal kaste feil fra compute`() = runTest {
        shouldThrow<IllegalStateException> {
            cache.computeAsync("test") { error("Error!") }
        }
    }

    @Test
    fun `Skal fjerne verdi fra cache`() = runTest {
        val key = "test"
        val i1 = cache.computeAsync(key) { nå() }.shouldNotBeNull()
        val i2 = cache.removeAsync(key).shouldNotBeNull()
        i1 shouldBe i2
    }
}
