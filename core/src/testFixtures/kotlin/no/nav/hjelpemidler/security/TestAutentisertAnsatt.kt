package no.nav.hjelpemidler.security

import no.nav.hjelpemidler.domain.tilgang.NavIdent
import java.util.UUID

object TestAutentisertAnsatt : AutentisertAnsatt {
    override val id: NavIdent = NavIdent.UKJENT
    override val navn: String = ""
    override val epost: String = ""
    override val grupper: Set<UUID> = emptySet()
    override val userToken: String = "userToken"
}
