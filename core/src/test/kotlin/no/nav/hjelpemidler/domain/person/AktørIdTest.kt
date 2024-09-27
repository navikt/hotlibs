package no.nav.hjelpemidler.domain.person

import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.types.shouldBeInstanceOf
import kotlin.test.Test

class AktørIdTest {
    @Test
    fun `AktørId skal bestå av 13 siffer`() {
        shouldThrow<IllegalArgumentException> { "".toAktørId() }
        shouldThrow<IllegalArgumentException> { "abcdefghijklm".toAktørId() }
        shouldThrow<IllegalArgumentException> { "123456789101".toAktørId() }
        shouldThrow<IllegalArgumentException> { "12345678910111".toAktørId() }
        shouldNotThrowAny { "1234567891011".toAktørId() }
        "1234567891011".toPersonIdent().shouldBeInstanceOf<AktørId>()
    }
}
