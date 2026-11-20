package no.nav.hjelpemidler.security

import no.nav.hjelpemidler.domain.tilgang.Applikasjonsnavn
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

@JvmInline
value class AutentisertApplikasjon(override val id: Applikasjonsnavn) : AuthenticatedPrincipal {
    override fun toString(): String = id.value
}

inline fun <T> AuthenticatedPrincipal.mapApplikasjonOrNull(block: (AutentisertApplikasjon) -> T): T? =
    if (this is AutentisertApplikasjon) block(this) else null

@OptIn(ExperimentalContracts::class)
fun AuthenticatedPrincipal.requireApplikasjon(): AutentisertApplikasjon {
    contract {
        returns() implies (this@requireApplikasjon is AutentisertApplikasjon)
    }
    return this as? AutentisertApplikasjon ?: error("Forventet AuthenticatedApplication, var ${javaClass.name}")
}

@OptIn(ExperimentalContracts::class)
val AuthenticatedPrincipal.isApplikasjon: Boolean
    get() {
        contract {
            returns(true) implies (this@isApplikasjon is AutentisertApplikasjon)
        }
        return this is AutentisertApplikasjon
    }
