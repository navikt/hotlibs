package no.nav.hjelpemidler.security

import no.nav.hjelpemidler.domain.id.StringId
import no.nav.hjelpemidler.domain.tilgang.NavIdent

object TestAuthenticatedUser : AuthenticatedUser {
    override val id: StringId = NavIdent.UKJENT
    override val userToken: String = "userToken"
}
