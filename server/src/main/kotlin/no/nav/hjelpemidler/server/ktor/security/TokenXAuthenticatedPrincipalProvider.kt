package no.nav.hjelpemidler.server.ktor.security

import io.ktor.server.application.ApplicationCall
import no.nav.hjelpemidler.security.AutentisertInnbygger

typealias TokenXAuthenticatedPrincipalProvider = AuthenticatedPrincipalProvider<TokenXCredential>

class DefaultTokenXAuthenticatedPrincipalProvider : TokenXAuthenticatedPrincipalProvider {
    override suspend fun invoke(call: ApplicationCall, credential: TokenXCredential): AutentisertInnbygger {
        val fnr = credential.fnr
        val level = credential.level
        return DefaultAutentisertInnbygger(fnr, level, userToken = call.bearerToken())
    }
}
