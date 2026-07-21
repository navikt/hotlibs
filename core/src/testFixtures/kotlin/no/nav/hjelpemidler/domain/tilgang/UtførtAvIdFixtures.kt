package no.nav.hjelpemidler.domain.tilgang

import no.nav.hjelpemidler.text.doubleQuoted

val navIdent = NavIdent("A123456")
val navIdentJson = navIdent.toString().doubleQuoted()

val applikasjonsnavn = Applikasjonsnavn("testApplication")
val applikasjonsnavnJson = applikasjonsnavn.toString().doubleQuoted()
