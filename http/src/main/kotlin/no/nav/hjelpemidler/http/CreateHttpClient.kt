package no.nav.hjelpemidler.http

import com.fasterxml.jackson.databind.ObjectMapper
import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.HttpClientEngineConfig
import io.ktor.client.engine.HttpClientEngineFactory
import io.ktor.client.engine.cio.CIO
import no.nav.hjelpemidler.serialization.jackson.jsonMapper

fun createHttpClient(
    engine: HttpClientEngine = CIO.create(),
    objectMapper: ObjectMapper = jsonMapper,
    block: HttpClientConfig<*>.() -> Unit = {},
): HttpClient =
    HttpClient(engine) {
        expectSuccess = false
        jackson(objectMapper)
        block()
    }

fun <T : HttpClientEngineConfig> createHttpClient(
    engineFactory: HttpClientEngineFactory<T>,
    objectMapper: ObjectMapper = jsonMapper,
    block: HttpClientConfig<T>.() -> Unit = {},
): HttpClient =
    HttpClient(engineFactory) {
        expectSuccess = false
        jackson(objectMapper)
        block()
    }
