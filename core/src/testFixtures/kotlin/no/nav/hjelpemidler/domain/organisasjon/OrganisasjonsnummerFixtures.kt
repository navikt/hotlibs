package no.nav.hjelpemidler.domain.organisasjon

import no.bekk.bekkopen.org.OrganisasjonsnummerCalculator

fun lagTilfeldigeOrganisasjonsnumre(antall: Int): List<Organisasjonsnummer> = OrganisasjonsnummerCalculator
    .getOrganisasjonsnummerList(antall)
    .map { Organisasjonsnummer(it.value) }

fun lagTilfeldigOrganisasjonsnummer(): Organisasjonsnummer = lagTilfeldigeOrganisasjonsnumre(1).single()
