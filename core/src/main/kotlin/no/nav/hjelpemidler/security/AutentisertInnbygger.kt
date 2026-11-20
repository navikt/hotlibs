package no.nav.hjelpemidler.security

import no.nav.hjelpemidler.domain.person.Fødselsnummer
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

interface AutentisertInnbygger : AutentisertPerson {
    override val id: Fødselsnummer
    val fnr: Fødselsnummer get() = id
    val level: IdPortenSecurityLevel
}

inline fun <T> AuthenticatedPrincipal.mapInnbyggerOrNull(block: (AutentisertInnbygger) -> T): T? =
    if (this is AutentisertInnbygger) block(this) else null

@OptIn(ExperimentalContracts::class)
fun AuthenticatedPrincipal.requireInnbygger(): AutentisertInnbygger {
    contract {
        returns() implies (this@requireInnbygger is AutentisertInnbygger)
    }
    return this as? AutentisertInnbygger ?: error("Forventet AutentisertInnbygger, var ${javaClass.name}")
}

@OptIn(ExperimentalContracts::class)
val AuthenticatedPrincipal.isInnbygger: Boolean
    get() {
        contract {
            returns(true) implies (this@isInnbygger is AutentisertInnbygger)
        }
        return this is AutentisertInnbygger
    }
