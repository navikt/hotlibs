package no.nav.hjelpemidler.domain.joark

import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import no.nav.hjelpemidler.domain.kodeverk.Fagsaksystem
import no.nav.hjelpemidler.serialization.jackson.jsonToValue
import org.junit.jupiter.api.Test

class JournalpostSakTest {
    @Test
    fun `JSON til GenerellSak`() {
        val sak = jsonToValue<JournalpostSak>("""{ "sakstype": "GENERELL_SAK" }""")
        sak.shouldBeInstanceOf<JournalpostSak.GenerellSak>()
    }

    @Test
    fun `JSON til Fagsak`() {
        val sak = jsonToValue<JournalpostSak>("""{ "sakstype": "FAGSAK", "fagsakId": "1", "fagsaksystem": "HOTSAK" }""")
        sak.shouldBeInstanceOf<JournalpostSak.Fagsak>()
        sak.fagsakId shouldBe "1"
        sak.fagsaksystem shouldBe Fagsaksystem.HJELPEMIDLER
    }
}
