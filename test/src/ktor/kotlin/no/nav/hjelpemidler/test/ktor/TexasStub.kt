package no.nav.hjelpemidler.test.ktor

import io.ktor.http.Url
import io.ktor.server.request.receiveParameters
import io.ktor.server.response.respond
import io.ktor.server.routing.post
import io.ktor.server.routing.routing
import io.ktor.server.testing.ExternalServicesBuilder
import io.ktor.server.util.getOrFail
import no.nav.hjelpemidler.configuration.TexasEnvironmentVariable
import kotlin.time.Duration.Companion.hours

fun ExternalServicesBuilder.texas(accessToken: String = "") = texas { tokenSet(accessToken) }

fun ExternalServicesBuilder.texas(tokenSetProvider: suspend () -> Map<String, Any?>) {
    val tokenUrl = Url(TexasEnvironmentVariable.NAIS_TOKEN_ENDPOINT)
    val exchangeUrl = Url(TexasEnvironmentVariable.NAIS_TOKEN_EXCHANGE_ENDPOINT)
    val introspectionUrl = Url(TexasEnvironmentVariable.NAIS_TOKEN_INTROSPECTION_ENDPOINT)
    baseUrl(TexasEnvironmentVariable.NAIS_TOKEN_ENDPOINT) {
        routing {
            post(tokenUrl.encodedPath) {
                call.respond(tokenSetProvider())
            }
            post(exchangeUrl.encodedPath) {
                val parameters = call.receiveParameters()
                val userToken = parameters.getOrFail("user_token")
                call.respond(tokenSet(userToken))
            }
            post(introspectionUrl.encodedPath) {
                call.respond(mapOf("active" to true))
            }
        }
    }
}

private fun tokenSet(accessToken: String = ""): Map<String, Any?> = mapOf(
    "access_token" to accessToken,
    "expires_in" to 1.hours.inWholeSeconds,
    "token_type" to "Bearer",
)
