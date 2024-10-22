package no.nav.hjelpemidler.domain.geografi

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import no.nav.hjelpemidler.test.testFactory
import org.junit.jupiter.api.TestFactory
import kotlin.test.Test

class KommuneTest {
    @Test
    fun `Kommuner med likt nummer er like`() {
        Kommune("1234", "A") shouldBe Kommune("1234", "B")
    }

    @Test
    fun `Kommuner med ulikt nummer er ulike`() {
        Kommune("0001", "A") shouldNotBe Kommune("0002", "A")
    }

    @TestFactory
    fun `Kommunenummer består av fire siffer`() = testFactory(
        listOf(
            "",
            "    ",
            "999",
            "99999",
            "\t9999\t",
            "abcd",
            "ABCD",
        ),
        { "Kommunenummer: '$it' er ugyldig" },
    ) {
        shouldThrow<IllegalArgumentException> { Kommune(it, "") }
    }
}
