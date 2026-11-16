package no.nav.hjelpemidler.server.ktor.security

import io.github.oshai.kotlinlogging.KotlinLogging
import io.ktor.server.application.ApplicationCall
import no.nav.hjelpemidler.security.AuthenticatedPrincipal

private val log = KotlinLogging.logger {}

typealias TexasAuthenticatedPrincipalProvider = AuthenticatedPrincipalProvider<TexasCredential>

class DefaultTexasAuthenticatedPrincipalProvider : TexasAuthenticatedPrincipalProvider {
    private val entraId = DefaultEntraIdAuthenticatedPrincipalProvider()
    private val tokenX = DefaultTokenXAuthenticatedPrincipalProvider()

    override suspend fun invoke(call: ApplicationCall, credential: TexasCredential): AuthenticatedPrincipal? =
        when (credential) {
            is EntraIdCredential -> entraId(call, credential)

            is IDPortenCredential -> {
                log.warn { "Støtte for IdentityProvider.ID_PORTEN er ikke ferdig implementert" }
                null
            }

            is MaskinportenCredential -> {
                log.warn { "Støtte for IdentityProvider.MASKINPORTEN er ikke ferdig implementert" }
                null
            }

            is TokenXCredential -> tokenX(call, credential)
        }
}
