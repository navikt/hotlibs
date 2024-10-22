package no.nav.hjelpemidler.domain.geografi

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import no.nav.hjelpemidler.test.testFactory
import org.junit.jupiter.api.TestFactory
import kotlin.test.Test

class BydelTest {
    @Test
    fun `Bydeler med likt nummer er like`() {
        Bydel("123456", "A") shouldBe Bydel("123456", "B")
    }

    @Test
    fun `Kommuner med ulikt nummer er ulike`() {
        Bydel("000001", "A") shouldNotBe Bydel("000002", "A")
    }

    @TestFactory
    fun `Bydelsnummer består av seks siffer`() = testFactory(
        listOf(
            "",
            "    ",
            "99999",
            "9999999",
            "\t999999\t",
            "abcdef",
            "ABCDEF",
        ),
        { "Bydelsnummer: '$it' er ugyldig" },
    ) {
        shouldThrow<IllegalArgumentException> { Bydel(it, "") }
    }
}
