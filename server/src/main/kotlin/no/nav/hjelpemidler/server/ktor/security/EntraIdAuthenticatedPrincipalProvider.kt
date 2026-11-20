package no.nav.hjelpemidler.server.ktor.security

import io.ktor.server.application.ApplicationCall
import no.nav.hjelpemidler.collections.mapToSet
import no.nav.hjelpemidler.security.AutentisertApplikasjon
import no.nav.hjelpemidler.security.AuthenticatedPrincipal

typealias EntraIdAuthenticatedPrincipalProvider = AuthenticatedPrincipalProvider<EntraIdCredential>

class DefaultEntraIdAuthenticatedPrincipalProvider : EntraIdAuthenticatedPrincipalProvider {
    override suspend fun invoke(call: ApplicationCall, credential: EntraIdCredential): AuthenticatedPrincipal =
        when (credential) {
            is EntraIdCredential.Application -> AutentisertApplikasjon(credential.id)
            is EntraIdCredential.User -> DefaultAutentisertAnsatt(
                credential.id,
                credential.navn,
                credential.epost,
                credential.grupper.mapToSet(DefaultAutentisertAnsatt::Gruppe),
                userToken = call.bearerToken(),
            )
        }
}
