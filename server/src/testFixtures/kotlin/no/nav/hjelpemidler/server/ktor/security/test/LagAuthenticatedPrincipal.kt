package no.nav.hjelpemidler.server.ktor.security.test

import no.nav.hjelpemidler.collections.mapToSet
import no.nav.hjelpemidler.domain.person.Fødselsnummer
import no.nav.hjelpemidler.domain.person.lagTilfeldigFødselsnummer
import no.nav.hjelpemidler.domain.tilgang.Applikasjonsnavn
import no.nav.hjelpemidler.domain.tilgang.NavIdent
import no.nav.hjelpemidler.domain.tilgang.lagTilfeldigNavIdent
import no.nav.hjelpemidler.security.AutentisertAnsatt
import no.nav.hjelpemidler.security.AutentisertApplikasjon
import no.nav.hjelpemidler.security.AutentisertInnbygger
import no.nav.hjelpemidler.security.IdPortenSecurityLevel
import no.nav.hjelpemidler.server.ktor.security.DefaultAutentisertAnsatt
import no.nav.hjelpemidler.server.ktor.security.DefaultAutentisertInnbygger
import java.util.UUID

val applikasjon = AutentisertApplikasjon(Applikasjonsnavn.GJELDENDE)
val applikasjonToken = applikasjon.asToken()

fun lagAutentisertAnsatt(
    id: NavIdent = lagTilfeldigNavIdent(),
    grupper: Set<UUID> = setOf(UUID.randomUUID()),
): AutentisertAnsatt {
    val ansatt = DefaultAutentisertAnsatt(
        id = id,
        navn = id.toString(),
        epost = "$id@trygdeetaten.no",
        grupper = grupper.mapToSet(DefaultAutentisertAnsatt::Gruppe),
        userToken = "",
    )
    return ansatt.copy(userToken = ansatt.asToken())
}

fun lagAutentisertInnbygger(
    fnr: Fødselsnummer = lagTilfeldigFødselsnummer(),
    level: IdPortenSecurityLevel = IdPortenSecurityLevel.HIGH,
): AutentisertInnbygger {
    val innbygger = DefaultAutentisertInnbygger(
        fnr = fnr,
        level = level,
        userToken = ""
    )
    return innbygger.copy(userToken = innbygger.asToken())
}
