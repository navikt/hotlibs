package no.nav.hjelpemidler.domain.person

import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.types.shouldBeInstanceOf
import no.nav.hjelpemidler.test.testFactory
import org.junit.jupiter.api.TestFactory
import kotlin.test.Test

class AktørIdTest {
    @Test
    fun `AktørId som består av tretten siffer er gyldig`() {
        shouldNotThrowAny { "1234567891011".toAktørId() }
        "1234567891011".toPersonIdent().shouldBeInstanceOf<AktørId>()
    }

    @TestFactory
    fun `AktørId som ikke består av tretten siffer er ugyldig`() = testFactory(
        sequenceOf(
            "",
            "             ",
            "abcdefghijklm",
            "ABCDEFGHIJKLM",
            "123456789101",
            "12345678910111",
        ),
        { "AktørId: '$it' er ugyldig" },
    ) {
        shouldThrow<IllegalArgumentException>(it::toAktørId)
    }
}
