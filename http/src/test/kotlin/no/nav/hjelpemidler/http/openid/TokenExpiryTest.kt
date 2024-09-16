package no.nav.hjelpemidler.http.openid

import io.kotest.matchers.types.shouldBeSameInstanceAs
import io.kotest.matchers.types.shouldNotBeSameInstanceAs
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import no.nav.hjelpemidler.cache.createCache
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

class TokenExpiryTest {
    private val key = "key"
    private val loader = mockk<(String) -> TokenSet>()
    private val cache = createCache()
        .expireAfter(TokenExpiry(leeway = 0.seconds))
        .build(loader)

    @BeforeTest
    fun setUp() {
        every {
            loader(key)
        } returns tokenSetThat(expiresIn = 0.seconds)
    }

    @Test
    fun `Henter nytt token`() {
        val tokenSet1 = tokenSetThat(expiresIn = 0.seconds)
        cache.put(key, tokenSet1)
        val tokenSet2 = cache.get(key, loader)

        tokenSet1 shouldNotBeSameInstanceAs tokenSet2

        verify(exactly = 1) {
            loader(key)
        }
    }

    @Test
    fun `Bruker token fra cache`() {
        val tokenSet1 = tokenSetThat(expiresIn = 1.minutes)
        cache.put(key, tokenSet1)
        val tokenSet2 = cache.get(key, loader)

        tokenSet1 shouldBeSameInstanceAs tokenSet2

        verify(exactly = 0) {
            loader(key)
        }
    }

    private fun tokenSetThat(expiresIn: Duration): TokenSet =
        TokenSet.bearer(
            expiresIn = expiresIn,
            accessToken = "token",
        )
}
