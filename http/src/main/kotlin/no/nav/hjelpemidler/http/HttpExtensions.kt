package no.nav.hjelpemidler.http

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.json.JsonMapper
import io.ktor.client.HttpClientConfig
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.logging.LoggingConfig
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.serialization.jackson.JacksonConverter
import no.nav.hjelpemidler.serialization.defaultJsonMapper

fun HttpClientConfig<*>.jackson(objectMapper: ObjectMapper) =
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
