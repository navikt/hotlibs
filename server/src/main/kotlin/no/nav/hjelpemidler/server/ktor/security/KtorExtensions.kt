package no.nav.hjelpemidler.server.ktor.security

import io.ktor.http.auth.AuthScheme
import io.ktor.http.auth.HttpAuthHeader
import io.ktor.server.application.ApplicationCall
import io.ktor.server.auth.parseAuthorizationHeader
import io.ktor.server.auth.principal
import no.nav.hjelpemidler.security.AuthenticatedPrincipal

fun HttpAuthHeader.bearerToken(): String? =
    if (this is HttpAuthHeader.Single && authScheme.equals(AuthScheme.Bearer, ignoreCase = true)) blob else null

fun ApplicationCall.bearerToken(): String {
    val authorizationHeader = request.parseAuthorizationHeader()
    return authorizationHeader?.bearerToken() ?: error("ApplicationCall mangler Bearer-token")
}

fun ApplicationCall.authenticatedPrincipal(): AuthenticatedPrincipal? = principal<AuthenticatedPrincipal>()
