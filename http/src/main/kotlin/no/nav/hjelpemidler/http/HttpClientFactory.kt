package no.nav.hjelpemidler.http

import com.fasterxml.jackson.databind.ObjectMapper
import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.cio.CIO
import no.nav.hjelpemidler.serialization.defaultJsonMapper

fun interface HttpClientFactory {
    operator fun invoke(block: HttpClientConfig<*>.() -> Unit): HttpClient
}

object DefaultHttpClientFactory : HttpClientFactory {
    private val engine: HttpClientEngine = CIO.create()
    private val objectMapper: ObjectMapper = defaultJsonMapper()
    override fun invoke(block: HttpClientConfig<*>.() -> Unit): HttpClient = createHttpClient(
        engine = engine,
        objectMapper = objectMapper,
        block = block,
    )
}

fun createHttpClientFactory(
    engine: HttpClientEngine,
    objectMapper: ObjectMapper = defaultJsonMapper(),
): HttpClientFactory = HttpClientFactory {
    createHttpClient(
        engine = engine,
        objectMapper = objectMapper,
        block = it,
    )
}
