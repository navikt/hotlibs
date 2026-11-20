package no.nav.hjelpemidler.server.ktor.security

import io.ktor.server.application.ApplicationCall
import no.nav.hjelpemidler.domain.tilgang.NavIdent
import no.nav.hjelpemidler.security.AutentisertAnsatt
import no.nav.hjelpemidler.security.requireAnsatt
import java.util.UUID

/**
 * En Nav-ansatt autentisert med Entra ID.
 */
data class DefaultAutentisertAnsatt(
    override val id: NavIdent,
    override val navn: String,
    override val epost: String,
    override val grupper: Set<Gruppe>,
    override val userToken: String,
) : AutentisertAnsatt {
    override fun toString(): String = "AutentisertAnsatt(id: $id)"

    @JvmInline
    value class Gruppe(override val id: UUID) : AutentisertAnsatt.Gruppe
}

fun ApplicationCall.autentisertAnsatt(): AutentisertAnsatt? = authenticatedPrincipal()?.requireAnsatt()
