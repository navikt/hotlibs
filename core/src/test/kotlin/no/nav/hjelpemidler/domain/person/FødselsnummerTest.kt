package no.nav.hjelpemidler.domain.person

import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import kotlin.test.Test

class FødselsnummerTest {
    @Test
    fun `Fødselsnummer skal være gyldig`() {
        shouldThrow<IllegalArgumentException> { Fødselsnummer("12345678910") }
        shouldNotThrowAny { Fødselsnummer(40.år) }
        personIdentOf(Fødselsnummer(40.år).toString()).shouldBeInstanceOf<Fødselsnummer>()
    }

    @Test
    fun `Fødselsnumre skal være like`() {
        val fnr1 = Fødselsnummer(30.år)
        val fnr2 = Fødselsnummer(fnr1.value)
        fnr1 shouldBe fnr2
        fnr1.hashCode() shouldBe fnr2.hashCode()
        fnr1.value shouldBe fnr2.value
        fnr1.toString() shouldBe fnr2.toString()
    }
}
