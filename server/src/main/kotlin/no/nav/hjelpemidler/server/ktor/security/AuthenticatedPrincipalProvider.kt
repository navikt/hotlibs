package no.nav.hjelpemidler.server.ktor.security

import io.ktor.http.auth.AuthScheme
import io.ktor.http.auth.HttpAuthHeader
import io.ktor.server.application.ApplicationCall
import io.ktor.server.auth.jwt.JWTPayloadHolder
import io.ktor.server.auth.parseAuthorizationHeader
import io.ktor.server.auth.principal
import no.nav.hjelpemidler.security.AuthenticatedApplication
import no.nav.hjelpemidler.security.AuthenticatedPrincipal

fun interface AuthenticatedPrincipalProvider<T : JWTPayloadHolder> {
    suspend operator fun invoke(call: ApplicationCall, credential: T): AuthenticatedPrincipal?
}

class DefaultEntraIdAuthenticatedPrincipalProvider : AuthenticatedPrincipalProvider<EntraIdCredential> {
    override suspend fun invoke(call: ApplicationCall, credential: EntraIdCredential): AuthenticatedPrincipal? =
        when (credential) {
            is EntraIdCredential.Applikasjon -> AuthenticatedApplication(credential.id)
            is EntraIdCredential.Ansatt -> AutentisertAnsatt(
                credential.id,
                credential.navn,
                credential.epost,
                credential.grupper,
                userToken = call.bearerToken(),
            )
        }
}

class DefaultTokenXAuthenticatedPrincipalProvider : AuthenticatedPrincipalProvider<TokenXCredential> {
    override suspend fun invoke(call: ApplicationCall, credential: TokenXCredential): AuthenticatedPrincipal? {
        val fnr = credential.fnr ?: return null
        val level = credential.level ?: return null
        return AutentisertInnbygger(
            fnr,
            level,
            userToken = call.bearerToken(),
        )
    }
}

internal fun ApplicationCall.bearerToken(): String {
    val authorizationHeader = request.parseAuthorizationHeader()
    return if (authorizationHeader is HttpAuthHeader.Single
        && authorizationHeader.authScheme.equals(AuthScheme.Bearer, ignoreCase = true)
    ) {
        authorizationHeader.blob
    } else {
        error("ApplicationCall mangler Bearer-token")
    }
}

fun ApplicationCall.authenticatedPrincipal(): AuthenticatedPrincipal? = principal<AuthenticatedPrincipal>()
