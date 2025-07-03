package no.nav.hjelpemidler.streams.serialization

import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import no.nav.hjelpemidler.domain.person.Fødselsnummer
import no.nav.hjelpemidler.domain.person.år
import no.nav.hjelpemidler.serialization.jackson.jsonMapper
import org.apache.kafka.common.serialization.Serdes
import org.junit.jupiter.api.Test

class SerdesTest {
    @Test
    fun `Tester serde`() {
        serde<Long>().shouldBeInstanceOf<Serdes.LongSerde>()
        serde<String>().shouldBeInstanceOf<Serdes.StringSerde>()
    }

    @Test
    fun `Tester jsonSerde`() {
        val serde = jsonSerde<Map<String, Any?>>()
        val data = mapOf("id" to 1, "user" to "X123456")
        val bytes = jsonMapper.writeValueAsBytes(data)

        with(serde.serializer()) {
            serialize("", data).shouldBe(bytes)
            serialize("", null).shouldBeNull()
        }

        with(serde.deserializer()) {
            deserialize("", bytes).shouldBe(data)
            deserialize("", null).shouldBeNull()
        }
    }

    @Test
    fun `Tester fødselsnummerSerde`() {
        val serde = fødselsnummerSerde()
        val fnr = Fødselsnummer(50.år)
        val bytes = fnr.toByteArray()

        with(serde.serializer()) {
            serialize("", fnr).shouldBe(bytes)
            serialize("", null).shouldBeNull()
        }

        with(serde.deserializer()) {
            deserialize("", bytes).shouldBe(fnr)
            deserialize("", null).shouldBeNull()
        }
    }
}
