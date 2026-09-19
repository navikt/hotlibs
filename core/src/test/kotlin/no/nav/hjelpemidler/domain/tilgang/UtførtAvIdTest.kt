package no.nav.hjelpemidler.domain.tilgang

import io.kotest.matchers.types.shouldBeInstanceOf
import kotlin.test.Test

class UtførtAvIdTest {
    @Test
    fun `Konverter String til UtførtAvId`() {
        UtførtAvId.of("A123456").shouldBeInstanceOf<NavIdent>()
        UtførtAvId.of("hm-saksbehandling").shouldBeInstanceOf<Applikasjonsnavn>()
    }
}
