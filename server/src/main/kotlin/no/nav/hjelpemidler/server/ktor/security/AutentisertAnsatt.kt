package no.nav.hjelpemidler.server.ktor.security

import io.ktor.server.application.ApplicationCall
import no.nav.hjelpemidler.domain.tilgang.NavIdent
import no.nav.hjelpemidler.security.AuthenticatedPrincipal
import no.nav.hjelpemidler.security.AuthenticatedUser
import java.util.UUID
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

/**
 * En Nav-ansatt autentisert med Entra ID.
 */
data class AutentisertAnsatt(
    override val id: NavIdent,
    val navn: String,
    val epost: String,
    val grupper: Set<UUID>,
    override val userToken: String,
) : AuthenticatedUser {
    override fun toString(): String = "AutentisertAnsatt(id: $id)"
}

inline fun <T> AuthenticatedPrincipal.mapAnsattOrNull(block: (AutentisertAnsatt) -> T): T? =
    if (this is AutentisertAnsatt) block(this) else null

@OptIn(ExperimentalContracts::class)
fun AuthenticatedPrincipal.requireAnsatt(): AutentisertAnsatt {
    contract {
        returns() implies (this@requireAnsatt is AutentisertAnsatt)
    }
    return this as? AutentisertAnsatt ?: error("Expected AutentisertAnsatt, got ${javaClass.name}")
}

@OptIn(ExperimentalContracts::class)
val AuthenticatedPrincipal.isAnsatt: Boolean
    get() {
        contract {
            returns(true) implies (this@isAnsatt is AutentisertAnsatt)
        }
        return this is AutentisertAnsatt
    }

fun ApplicationCall.autentisertAnsatt(): AutentisertAnsatt? = authenticatedPrincipal()?.requireAnsatt()
