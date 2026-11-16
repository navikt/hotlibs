package no.nav.hjelpemidler.server.ktor.security

import io.ktor.server.application.ApplicationCall
import no.nav.hjelpemidler.security.AuthenticatedPrincipal

typealias TokenXAuthenticatedPrincipalProvider = AuthenticatedPrincipalProvider<TokenXCredential>

class DefaultTokenXAuthenticatedPrincipalProvider : TokenXAuthenticatedPrincipalProvider {
    override suspend fun invoke(call: ApplicationCall, credential: TokenXCredential): AuthenticatedPrincipal {
        val fnr = credential.fnr
        val level = credential.level
        return AutentisertInnbygger(fnr, level, userToken = call.bearerToken())
    }
}
