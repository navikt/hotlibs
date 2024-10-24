package no.nav.hjelpemidler.serialization

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.databind.json.JsonMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonMapperBuilder
import no.nav.hjelpemidler.configuration.Environment

/**
 * Definerer standardinnstillinger for [JsonMapper].
 * * Legger til [JavaTimeModule] for å støtte java.time.* (JSR310).
 * * Skrur av [SerializationFeature.WRITE_DATES_AS_TIMESTAMPS] for at datoer og tidspunkt skal serialiseres som tekst.
 * * Skrur av [DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES] for å tillate egenskaper i JSON som ikke finnes i Kotlin-klassen det mappes til.
 * * Skrur på [JsonParser.Feature.INCLUDE_SOURCE_IN_LOCATION] hvis IKKE produksjon slik at kilde-JSON inkluderes ved feil under deserialisering.
 *
 * @see [defaultJsonMapper]
 */
fun JsonMapper.Builder.default(): JsonMapper.Builder =
    this
        .addModule(JavaTimeModule())
        .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
        .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
        .configure(JsonParser.Feature.INCLUDE_SOURCE_IN_LOCATION, !Environment.current.isProd)

/**
 * Opprett [JsonMapper] med standardinnstillinger.
 * @see [JsonMapper.Builder.default]
 */
fun defaultJsonMapper(block: JsonMapper.Builder.() -> Unit = {}): JsonMapper =
    jacksonMapperBuilder()
        .default()
        .apply(block)
        .build()
