package no.nav.hjelpemidler.domain.tilgang

import io.kotest.matchers.types.shouldBeInstanceOf
import kotlin.test.Test

class UtførtAvIdTest {
    @Test
    fun `Konverter String til UtførtAvId`() {
        utførtAvIdOf("A123456").shouldBeInstanceOf<NavIdent>()
        utførtAvIdOf("hm-saksbehandling").shouldBeInstanceOf<Systemnavn>()
    }
}
