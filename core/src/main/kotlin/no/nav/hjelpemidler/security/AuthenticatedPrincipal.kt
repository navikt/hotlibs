package no.nav.hjelpemidler.security

import no.nav.hjelpemidler.domain.id.StringId
import java.security.Principal

sealed interface AuthenticatedPrincipal : Principal {
    val id: StringId

    override fun getName(): String = id.value
}
