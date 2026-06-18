package no.nav.hjelpemidler.test.ktor

import io.ktor.client.engine.HttpClientEngine
import io.ktor.http.ContentType
import io.ktor.http.Url
import io.ktor.http.protocolWithAuthority
import io.ktor.serialization.jackson3.JacksonConverter
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.config.MapApplicationConfig
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.plugins.statuspages.StatusPages
import io.ktor.server.request.httpMethod
import io.ktor.server.request.uri
import io.ktor.server.testing.ApplicationTestBuilder
import io.ktor.server.testing.ExternalServicesBuilder
import no.nav.hjelpemidler.serialization.jackson.jsonMapper
import org.slf4j.LoggerFactory

private val log = LoggerFactory.getLogger("ExternalServices")

/**
 * Lag en Ktor-applikasjon som kan fungere som en stub av eksterne tjenester og returner en [HttpClientEngine] for å kommunisere med denne.
 */
@Suppress("FunctionName")
fun StubEngine(block: ExternalServicesBuilder.() -> Unit): HttpClientEngine {
    val builder = ApplicationTestBuilder().apply {
        environment { config = MapApplicationConfig() }
        externalServices { block() }
    }
    return builder.client.engine
}

/**
 * Lag stub for ekstern tjeneste.
 */
fun ExternalServicesBuilder.baseUrl(
    urlString: String,
    block: Application.(baseUrl: String) -> Unit,
) {
    val url = Url(urlString)
    val host = url.protocolWithAuthority
    val path = urlString.removePrefix(host)
    hosts(host) {
        install(ContentNegotiation) {
            register(ContentType.Application.Json, JacksonConverter(jsonMapper))
        }
        install(StatusPages) {
            unhandled {
                log.warn("Mangler svar for: '${it.request.httpMethod.value} ${it.request.uri}'")
            }
        }
        block(path)
    }
}
