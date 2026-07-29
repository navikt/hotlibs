package no.nav.hjelpemidler.domain.tilgang

import io.kotest.matchers.types.shouldBeInstanceOf
import kotlin.test.Test

class UtførtAvIdTest {
    @Test
    fun `Konverter String til UtførtAvId`() {
        UtførtAvId.from("A123456").shouldBeInstanceOf<NavIdent>()
        UtførtAvId.from("hm-saksbehandling").shouldBeInstanceOf<Applikasjonsnavn>()
    }
}
