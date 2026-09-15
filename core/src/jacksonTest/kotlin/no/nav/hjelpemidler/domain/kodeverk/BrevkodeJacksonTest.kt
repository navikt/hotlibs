package no.nav.hjelpemidler.domain.kodeverk

import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import no.nav.hjelpemidler.serialization.jackson.jsonToValue
import no.nav.hjelpemidler.serialization.jackson.valueToJson
import no.nav.hjelpemidler.text.doubleQuoted
import org.junit.jupiter.api.Test

class BrevkodeJacksonTest {
    @Test
    fun `Kode som ikke er Nav-skjema blir UkjentBrevkode`() {
        val kode = "HJE_NOT_001"

        val brevkode = jsonToValue<Brevkode>(kode.doubleQuoted())
        brevkode.shouldBeInstanceOf<UkjentBrevkode>()
        brevkode.kode shouldBe kode

        valueToJson(brevkode) shouldBe kode.doubleQuoted()
    }

    @Test
    fun `Kode som er ukjent Nav-skjema blir UkjentNavSkjema`() {
        val kode = "NAV 12-06.05"

        val brevkode = jsonToValue<Brevkode>(kode.doubleQuoted())
        brevkode.shouldBeInstanceOf<UkjentNavSkjema>()
        brevkode.kode shouldBe kode

        valueToJson(brevkode) shouldBe kode.doubleQuoted()
    }

    @Test
    fun `Kode som er kjent Nav-skjema blir NavSkjema`() {
        val kode = "NAV 10-07.03"

        val brevkode = jsonToValue<Brevkode>(kode.doubleQuoted())
        brevkode.shouldBeInstanceOf<NavSkjema>()
        brevkode shouldBe NavSkjema.NAV_10_07_03

        valueToJson(brevkode) shouldBe kode.doubleQuoted()
    }
}
