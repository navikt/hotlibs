package no.nav.hjelpemidler.server.ktor.security

import io.ktor.server.application.ApplicationCall
import no.nav.hjelpemidler.security.AuthenticatedApplication
import no.nav.hjelpemidler.security.AuthenticatedPrincipal

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
