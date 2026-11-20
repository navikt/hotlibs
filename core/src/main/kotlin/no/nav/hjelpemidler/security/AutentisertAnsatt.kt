package no.nav.hjelpemidler.security

import no.nav.hjelpemidler.domain.tilgang.NavIdent
import java.util.UUID
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

interface AutentisertAnsatt : AutentisertPerson {
    override val id: NavIdent
    val navn: String
    val epost: String
    val grupper: Set<UUID>
}

inline fun <T> AuthenticatedPrincipal.mapAnsattOrNull(block: (AutentisertAnsatt) -> T): T? =
    if (this is AutentisertAnsatt) block(this) else null

@OptIn(ExperimentalContracts::class)
fun AuthenticatedPrincipal.requireAnsatt(): AutentisertAnsatt {
    contract {
        returns() implies (this@requireAnsatt is AutentisertAnsatt)
    }
    return this as? AutentisertAnsatt ?: error("Forventet AutentisertAnsatt, var ${javaClass.name}")
}

@OptIn(ExperimentalContracts::class)
val AuthenticatedPrincipal.isAnsatt: Boolean
    get() {
        contract {
            returns(true) implies (this@isAnsatt is AutentisertAnsatt)
        }
        return this is AutentisertAnsatt
    }
