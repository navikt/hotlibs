package no.nav.hjelpemidler.serialization.jackson

import io.github.oshai.kotlinlogging.KotlinLogging
import no.nav.hjelpemidler.configuration.Environment
import tools.jackson.core.StreamReadFeature
import tools.jackson.databind.DeserializationFeature
import tools.jackson.databind.JacksonModule
import tools.jackson.databind.ObjectMapper
import tools.jackson.databind.cfg.DateTimeFeature
import tools.jackson.databind.json.JsonMapper
import tools.jackson.module.kotlin.jacksonMapperBuilder

private val log = KotlinLogging.logger {}

val threeTenExtraModule: JacksonModule by lazy(::ThreeTenExtraModule)

/**
 * Definerer standardinnstillinger for [JsonMapper].
 * * Legger til [ThreeTenExtraModule] for å støtte org.threeten.extra.* (ThreeTen-Extra).
 * * Skrur av [DateTimeFeature.WRITE_DATES_AS_TIMESTAMPS] for at datoer og tidspunkt skal serialiseres som tekst.
 * * Skrur av [DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES] for å tillate egenskaper i JSON som ikke finnes i Kotlin-klassen det mappes til.
 * * Skrur på [StreamReadFeature.INCLUDE_SOURCE_IN_LOCATION] hvis IKKE produksjon slik at kilde-JSON inkluderes ved feil under deserialisering.
 *
 * @see [defaultJsonMapper]
 */
fun JsonMapper.Builder.default(): JsonMapper.Builder {
    return this
        .addModule(threeTenExtraModule)
        .disable(DateTimeFeature.WRITE_DATES_AS_TIMESTAMPS)
        .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
        .configure(StreamReadFeature.INCLUDE_SOURCE_IN_LOCATION, !Environment.current.isProd)
}

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
