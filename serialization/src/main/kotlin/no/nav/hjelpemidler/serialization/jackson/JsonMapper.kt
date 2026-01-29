package no.nav.hjelpemidler.serialization.jackson

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.databind.json.JsonMapper
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonMapperBuilder
import io.github.oshai.kotlinlogging.KotlinLogging
import no.nav.hjelpemidler.configuration.Environment

private val log = KotlinLogging.logger {}

/**
 * Definerer standardinnstillinger for [JsonMapper].
 * * Legger til [Jdk8Module] for å støtte java.util.Optional.
 * * Legger til [JavaTimeModule] for å støtte java.time.* (JSR 310).
 * * Skrur av [SerializationFeature.WRITE_DATES_AS_TIMESTAMPS] for at datoer og tidspunkt skal serialiseres som tekst.
 * * Skrur av [DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES] for å tillate egenskaper i JSON som ikke finnes i Kotlin-klassen det mappes til.
 * * Skrur på [JsonParser.Feature.INCLUDE_SOURCE_IN_LOCATION] hvis IKKE produksjon slik at kilde-JSON inkluderes ved feil under deserialisering.
 *
 * @see [defaultJsonMapper]
 */
fun JsonMapper.Builder.default(): JsonMapper.Builder =
    this
        .addModule(Jdk8Module())
        .addModule(JavaTimeModule())
        .addModule(ThreeTenExtraModule())
        .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
        .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
        .configure(JsonParser.Feature.INCLUDE_SOURCE_IN_LOCATION, !Environment.current.isProd)

/**
 * Opprett [JsonMapper] med standardinnstillinger.
 * @see [JsonMapper.Builder.default]
 */
fun defaultJsonMapper(block: JsonMapper.Builder.() -> Unit = {}): ObjectMapper =
    jacksonMapperBuilder()
        .default()
        .apply(block)
        .build()
        .also { log.debug { "Opprettet ObjectMapper: ${it.hashCode()}" } }

/**
 * [ObjectMapper] som bruker [JacksonObjectMapperProvider] for å opprette en instans.
 */
val jsonMapper: ObjectMapper by lazy(jacksonObjectMapperProvider::invoke)
