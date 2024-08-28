package no.nav.hjelpemidler.http

import com.fasterxml.jackson.databind.ObjectMapper
import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.cio.CIO
import no.nav.hjelpemidler.serialization.defaultJsonMapper

fun createHttpClient(
    engine: HttpClientEngine = CIO.create(),
    objectMapper: ObjectMapper = defaultJsonMapper(),
    block: HttpClientConfig<*>.() -> Unit = {},
): HttpClient =
    HttpClient(engine) {
        expectSuccess = false
        jackson(objectMapper)
        block()
    }
