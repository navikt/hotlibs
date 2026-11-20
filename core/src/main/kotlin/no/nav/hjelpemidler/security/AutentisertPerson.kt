package no.nav.hjelpemidler.security

import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

sealed interface AutentisertPerson : AuthenticatedPrincipal {
    /**
     * User token for token exchange.
     */
    val userToken: String
}

inline fun <T> AuthenticatedPrincipal.mapPersonOrNull(block: (AutentisertPerson) -> T): T? =
    if (this is AutentisertPerson) block(this) else null

@OptIn(ExperimentalContracts::class)
fun AuthenticatedPrincipal.requirePerson(): AutentisertPerson {
    contract {
        returns() implies (this@requirePerson is AutentisertPerson)
    }
    return this as? AutentisertPerson
        ?: error("Forventet AutentisertPerson, var ${javaClass.name}")
}

@OptIn(ExperimentalContracts::class)
val AuthenticatedPrincipal.isPerson: Boolean
    get() {
        contract {
            returns(true) implies (this@isPerson is AutentisertPerson)
        }
        return this is AutentisertPerson
    }
