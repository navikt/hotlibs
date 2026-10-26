package no.nav.hjelpemidler.server.ktor.security.test

import no.nav.hjelpemidler.domain.person.lagTilfeldigFødselsnummer
import no.nav.hjelpemidler.domain.tilgang.Applikasjonsnavn
import no.nav.hjelpemidler.domain.tilgang.lagTilfeldigNavIdent
import no.nav.hjelpemidler.security.AuthenticatedApplication
import no.nav.hjelpemidler.server.ktor.security.AutentisertAnsatt
import no.nav.hjelpemidler.server.ktor.security.AutentisertInnbygger
import no.nav.hjelpemidler.server.ktor.security.SecurityLevelOfAssurance
import java.util.UUID

val applikasjon = AuthenticatedApplication(Applikasjonsnavn.GJELDENDE)
val applikasjonToken = applikasjon.asToken()

fun lagAutentisertAnsatt(): AutentisertAnsatt {
    val ansatt = AutentisertAnsatt(
        id = lagTilfeldigNavIdent(),
        navn = "Sak S. Behandler",
        epost = "sak.s.behandler@nav.no",
        grupper = setOf(UUID.randomUUID()),
        userToken = "",
    )
    return ansatt.copy(userToken = ansatt.asToken())
}

fun lagAutentisertInnbygger(): AutentisertInnbygger {
    val innbygger = AutentisertInnbygger(
        fnr = lagTilfeldigFødselsnummer(),
        level = SecurityLevelOfAssurance.HIGH,
        userToken = ""
    )
    return innbygger.copy(userToken = innbygger.asToken())
}
