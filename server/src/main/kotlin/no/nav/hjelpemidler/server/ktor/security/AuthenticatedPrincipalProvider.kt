package no.nav.hjelpemidler.server.ktor.security

import io.ktor.server.application.ApplicationCall
import no.nav.hjelpemidler.security.AuthenticatedApplication
import no.nav.hjelpemidler.security.AuthenticatedPrincipal

fun interface AuthenticatedPrincipalProvider<T : TexasCredential> {
    suspend operator fun invoke(call: ApplicationCall, credential: T): AuthenticatedPrincipal?
}

typealias EntraIdAuthenticatedPrincipalProvider = AuthenticatedPrincipalProvider<EntraIdCredential>

class DefaultEntraIdAuthenticatedPrincipalProvider : EntraIdAuthenticatedPrincipalProvider {
    override suspend fun invoke(call: ApplicationCall, credential: EntraIdCredential): AuthenticatedPrincipal =
        when (credential) {
            is EntraIdCredential.Application -> AuthenticatedApplication(credential.id)
            is EntraIdCredential.User -> AutentisertAnsatt(
                credential.id,
                credential.navn,
                credential.epost,
                credential.grupper,
                userToken = call.bearerToken(),
            )
        }
}

typealias TokenXAuthenticatedPrincipalProvider = AuthenticatedPrincipalProvider<TokenXCredential>

class DefaultTokenXAuthenticatedPrincipalProvider : TokenXAuthenticatedPrincipalProvider {
    override suspend fun invoke(call: ApplicationCall, credential: TokenXCredential): AuthenticatedPrincipal {
        val fnr = credential.fnr
        val level = credential.level
        return AutentisertInnbygger(fnr, level, userToken = call.bearerToken())
    }
}
