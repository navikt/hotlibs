package no.nav.hjelpemidler.server.ktor.security.test

import no.nav.hjelpemidler.domain.person.Fødselsnummer
import no.nav.hjelpemidler.domain.person.lagTilfeldigFødselsnummer
import no.nav.hjelpemidler.domain.tilgang.Applikasjonsnavn
import no.nav.hjelpemidler.domain.tilgang.NavIdent
import no.nav.hjelpemidler.domain.tilgang.lagTilfeldigNavIdent
import no.nav.hjelpemidler.security.AuthenticatedApplication
import no.nav.hjelpemidler.server.ktor.security.AutentisertAnsatt
import no.nav.hjelpemidler.server.ktor.security.AutentisertInnbygger
import no.nav.hjelpemidler.server.ktor.security.IdPortenSecurityLevel
import java.util.UUID

val applikasjon = AuthenticatedApplication(Applikasjonsnavn.GJELDENDE)
val applikasjonToken = applikasjon.asToken()

fun lagAutentisertAnsatt(
    id: NavIdent = lagTilfeldigNavIdent(),
    grupper: Set<UUID> = setOf(UUID.randomUUID()),
): AutentisertAnsatt {
    val ansatt = AutentisertAnsatt(
        id = id,
        navn = id.toString(),
        epost = "$id@trygdeetaten.no",
        grupper = grupper,
        userToken = "",
    )
    return ansatt.copy(userToken = ansatt.asToken())
}

fun lagAutentisertInnbygger(
    fnr: Fødselsnummer = lagTilfeldigFødselsnummer(),
    level: IdPortenSecurityLevel = IdPortenSecurityLevel.HIGH,
): AutentisertInnbygger {
    val innbygger = AutentisertInnbygger(
        fnr = fnr,
        level = level,
        userToken = ""
    )
    return innbygger.copy(userToken = innbygger.asToken())
}
