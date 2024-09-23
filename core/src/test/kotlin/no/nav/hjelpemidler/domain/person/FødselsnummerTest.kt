package no.nav.hjelpemidler.domain.person

import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.assertions.throwables.shouldThrow
import kotlin.test.Test

class FødselsnummerTest {
    @Test
    fun `Fødselsnummer skal være gyldig`() {
        shouldThrow<IllegalArgumentException> { Fødselsnummer("12345678910") }
        shouldNotThrowAny { Fødselsnummer(40.år) }
    }
}
