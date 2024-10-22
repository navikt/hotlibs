package no.nav.hjelpemidler.domain.geografi

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import no.nav.hjelpemidler.test.testFactory
import org.junit.jupiter.api.TestFactory
import kotlin.test.Test

class EnhetTest {
    @Test
    fun `Enheter med likt nummer er like`() {
        Enhet("1234", "A") shouldBe Enhet("1234", "B")
    }

    @Test
    fun `Enheter med ulikt nummer er ulike`() {
        Enhet("0001", "A") shouldNotBe Enhet("0002", "A")
    }

    @TestFactory
    fun `Enhetsnummer består av fire siffer`() = testFactory(
        listOf(
            "",
            "    ",
            "999",
            "99999",
            "\t9999\t",
            "abcd",
            "ABCD",
        ),
        { "Enhetsnummer: '$it' er ugyldig" },
    ) {
        shouldThrow<IllegalArgumentException> { Enhet(it, "") }
    }
}
