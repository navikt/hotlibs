package no.nav.hjelpemidler.database

import no.nav.hjelpemidler.domain.enhet.Enhet
import no.nav.hjelpemidler.domain.geografi.Bydel
import no.nav.hjelpemidler.domain.geografi.Kommune
import no.nav.hjelpemidler.domain.person.Personnavn

fun Personnavn?.toQueryParameters(prefix: String? = null): QueryParameters = mapOf(
    "fornavn" to this?.fornavn,
    "mellomnavn" to this?.mellomnavn,
    "etternavn" to this?.etternavn,
).transform(prefix)

fun Enhet.toQueryParameters(): QueryParameters =
    mapOf("enhetsnummer" to nummer, "enhetsnavn" to navn)

fun Kommune.toQueryParameters(prefix: String? = null): QueryParameters = mapOf(
    "kommunenummer" to nummer,
    "kommunenavn" to navn,
).transform(prefix = prefix)

fun Bydel.toQueryParameters(prefix: String? = null): QueryParameters = mapOf(
    "bydelsnummer" to nummer,
    "bydelsnavn" to navn,
).transform(prefix = prefix)
