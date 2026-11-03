package no.nav.hjelpemidler.oppgave.kodeverk

import io.kotest.assertions.json.shouldEqualJson
import io.kotest.matchers.shouldBe
import no.nav.hjelpemidler.domain.kodeverk.JsonSerializeEksterntKodeverk
import no.nav.hjelpemidler.serialization.jackson.jsonToValue
import no.nav.hjelpemidler.serialization.jackson.valueToJson
import no.nav.hjelpemidler.text.doubleQuoted
import org.junit.jupiter.api.Test

class OppgaveprioritetTest {
    @Test
    fun `Håndterer ekstern kode ved deserialisering`() {
        jsonToValue<Oppgaveprioritet>("HOY".doubleQuoted()) shouldBe Oppgaveprioritet.HØY
        jsonToValue<Oppgaveprioritet>("NORM".doubleQuoted()) shouldBe Oppgaveprioritet.NORMAL
    }

    @Test
    fun `Benytter ekstern kode ved serialisering`() {
        valueToJson(Request(Oppgaveprioritet.HØY)) shouldEqualJson """{ "prioritet": "HOY" }"""
        valueToJson(Request(Oppgaveprioritet.NORMAL)) shouldEqualJson """{ "prioritet": "NORM" }"""
    }
}

private data class Request(
    @JsonSerializeEksterntKodeverk
    val prioritet: Oppgaveprioritet,
)
