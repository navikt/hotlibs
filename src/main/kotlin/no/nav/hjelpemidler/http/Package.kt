package no.nav.hjelpemidler.http

import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.cio.CIO

fun createHttpClient(
    engine: HttpClientEngine = CIO.create(),
    block: HttpClientConfig<*>.() -> Unit = {},
): HttpClient =
    HttpClient(engine = engine) {
        expectSuccess = false
        jackson()
        block()
    }
