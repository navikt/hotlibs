package no.nav.hjelpemidler.server.ktor.security

import io.ktor.server.application.ApplicationCall
import no.nav.hjelpemidler.security.AuthenticatedPrincipal

fun interface AuthenticatedPrincipalProvider<T : TexasCredential> {
    suspend operator fun invoke(call: ApplicationCall, credential: T): AuthenticatedPrincipal?
}
