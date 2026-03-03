package no.nav.hjelpemidler.http

import io.ktor.client.HttpClientConfig
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.logging.LoggingConfig
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.serialization.jackson3.JacksonConverter
import no.nav.hjelpemidler.serialization.jackson.defaultJsonMapper
import no.nav.hjelpemidler.serialization.jackson.jsonMapper
import tools.jackson.databind.ObjectMapper
import tools.jackson.databind.json.JsonMapper

fun HttpClientConfig<*>.jackson(objectMapper: ObjectMapper = jsonMapper) =
    install(ContentNegotiation) {
        register(ContentType.Application.Json, JacksonConverter(objectMapper, streamRequestBody = true))
    }

fun HttpClientConfig<*>.jackson(block: JsonMapper.Builder.() -> Unit = {}) =
    jackson(defaultJsonMapper(block))

fun HttpClientConfig<*>.logging(configure: LoggingConfig.() -> Unit = {}) =
    install(Logging) {
        configure()
        sanitizeHeader { header ->
            header == HttpHeaders.Authorization
        }
    }
