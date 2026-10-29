package no.nav.hjelpemidler.server.ktor.security

import io.github.oshai.kotlinlogging.KotlinLogging
import io.ktor.server.application.ApplicationCall
import io.ktor.server.auth.jwt.JWTPayloadHolder
import no.nav.hjelpemidler.security.AuthenticatedApplication
import no.nav.hjelpemidler.security.AuthenticatedPrincipal

private val log = KotlinLogging.logger { }

fun interface AuthenticatedPrincipalProvider<T : JWTPayloadHolder> {
    suspend operator fun invoke(call: ApplicationCall, credential: T): AuthenticatedPrincipal?
}

interface EntraIdAuthenticatedPrincipalProvider : AuthenticatedPrincipalProvider<EntraIdCredential>

class DefaultEntraIdAuthenticatedPrincipalProvider : EntraIdAuthenticatedPrincipalProvider {
    override suspend fun invoke(call: ApplicationCall, credential: EntraIdCredential): AuthenticatedPrincipal? =
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

interface TokenXAuthenticatedPrincipalProvider : AuthenticatedPrincipalProvider<TokenXCredential>

class DefaultTokenXAuthenticatedPrincipalProvider : TokenXAuthenticatedPrincipalProvider {
    override suspend fun invoke(call: ApplicationCall, credential: TokenXCredential): AuthenticatedPrincipal? {
        val fnr = credential.fnr ?: run {
            log.warn { "TokenXCredential mangler fnr" }
            return null
        }
        val level = credential.level ?: run {
            log.warn { "TokenXCredential mangler level" }
            return null
        }
        return AutentisertInnbygger(
            fnr,
            level,
            userToken = call.bearerToken(),
        )
    }
}
