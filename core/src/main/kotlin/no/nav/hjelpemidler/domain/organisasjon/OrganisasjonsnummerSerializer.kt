package no.nav.hjelpemidler.domain.organisasjon

import no.nav.hjelpemidler.domain.id.IdSerializer

object OrganisasjonsnummerSerializer : IdSerializer<Organisasjonsnummer>(
    serialName = "no.nav.hjelpemidler.domain.organisasjon.OrganisasjonsnummerSerializer",
    creator = ::Organisasjonsnummer,
)
