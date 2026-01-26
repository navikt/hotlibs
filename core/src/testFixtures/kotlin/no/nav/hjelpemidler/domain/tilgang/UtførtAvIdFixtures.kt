package no.nav.hjelpemidler.domain.tilgang

import no.nav.hjelpemidler.text.doubleQuoted

val navIdent = NavIdent("A123456")
val navIdentJson = navIdent.toString().doubleQuoted()

val systemnavn = Systemnavn("hm-saksbehandling")
val systemnavnJson = systemnavn.toString().doubleQuoted()
