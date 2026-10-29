package no.nav.hjelpemidler.server.ktor.security

import io.ktor.server.application.ApplicationCall
import no.nav.hjelpemidler.domain.person.Fødselsnummer
import no.nav.hjelpemidler.security.AuthenticatedPrincipal
import no.nav.hjelpemidler.security.AuthenticatedUser
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

/**
 * En innbygger (f.eks. formidler) autentisert med TokenX/ID-porten via en annen app.
 *
 * @see [TokenXCredential]
 */
data class AutentisertInnbygger(
    val fnr: Fødselsnummer,
    val level: IdPortenSecurityLevel,
    override val userToken: String,
) : AuthenticatedUser {
    override val id: Fødselsnummer get() = fnr

    override fun toString(): String = "AutentisertInnbygger"
}

inline fun <T> AuthenticatedPrincipal.mapInnbyggerOrNull(block: (AutentisertInnbygger) -> T): T? =
    if (this is AutentisertInnbygger) block(this) else null

@OptIn(ExperimentalContracts::class)
fun AuthenticatedPrincipal.requireInnbygger(): AutentisertInnbygger {
    contract {
        returns() implies (this@requireInnbygger is AutentisertInnbygger)
    }
    return this as? AutentisertInnbygger ?: error("Expected AutentisertInnbygger, got ${javaClass.name}")
}

@OptIn(ExperimentalContracts::class)
val AuthenticatedPrincipal.isInnbygger: Boolean
    get() {
        contract {
            returns(true) implies (this@isInnbygger is AutentisertInnbygger)
        }
        return this is AutentisertInnbygger
    }

fun ApplicationCall.autentisertInnbygger(): AutentisertInnbygger? = authenticatedPrincipal()?.requireInnbygger()
