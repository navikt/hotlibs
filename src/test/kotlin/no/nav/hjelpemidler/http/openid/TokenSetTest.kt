package no.nav.hjelpemidler.http.openid

import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockkStatic
import no.nav.hjelpemidler.http.test.createJWT
import no.nav.hjelpemidler.time.now
import java.time.LocalDate
import java.time.Month
import java.time.ZoneId
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

class TokenSetTest {
    private val at = LocalDate.of(2022, Month.AUGUST, 1)
        .atTime(12, 0)
        .atZone(ZoneId.systemDefault())
        .toInstant()

    @BeforeTest
    fun setUp() {
        mockkStatic(::now)
        every {
            now()
        } returns at
    }

    @Test
    fun `er utløpt når expiresIn er 0 sekunder`() {
        val tokenSet = tokenSetThat(expiresIn = 0.seconds)
        tokenSet.isExpired(at = at, leeway = 0.seconds) shouldBe true
    }

    @Test
    fun `er utløpt når expiresIn er 1 sekund med leeway 1 sekund`() {
        val tokenSet = tokenSetThat(expiresIn = 1.seconds)
        tokenSet.isExpired(at = at, leeway = 1.seconds) shouldBe true
    }

    @Test
    fun `er ikke utløpt når expiresIn er 1`() {
        val tokenSet = tokenSetThat(expiresIn = 1.seconds)
        tokenSet.isExpired(at = at, leeway = 0.seconds) shouldBe false
    }

    @Test
    fun `er ikke utløpt når expiresIn er 2 med leeway 1`() {
        val tokenSet = tokenSetThat(expiresIn = 2.seconds)
        tokenSet.isExpired(at = at, leeway = 1.seconds) shouldBe false
    }

    private fun tokenSetThat(expiresIn: Duration): TokenSet =
        TokenSet.bearer(
            expiresIn = expiresIn,
            accessToken = createJWT(),
        )
}
