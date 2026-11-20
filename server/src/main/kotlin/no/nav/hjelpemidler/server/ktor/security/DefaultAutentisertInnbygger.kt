package no.nav.hjelpemidler.server.ktor.security

import io.ktor.server.application.ApplicationCall
import no.nav.hjelpemidler.domain.person.Fødselsnummer
import no.nav.hjelpemidler.security.AutentisertInnbygger
import no.nav.hjelpemidler.security.IdPortenSecurityLevel
import no.nav.hjelpemidler.security.requireInnbygger

/**
 * En innbygger (f.eks. formidler) autentisert med TokenX/ID-porten via en annen app.
 *
 * @see [TokenXCredential]
 */
data class DefaultAutentisertInnbygger(
    override val fnr: Fødselsnummer,
    override val level: IdPortenSecurityLevel,
    override val userToken: String,
) : AutentisertInnbygger {
    override val id: Fødselsnummer get() = fnr

    override fun toString(): String = "AutentisertInnbygger"
}

fun ApplicationCall.autentisertInnbygger(): AutentisertInnbygger? = authenticatedPrincipal()?.requireInnbygger()
