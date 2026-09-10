package no.nav.hjelpemidler.domain.joark

import no.nav.hjelpemidler.domain.kodeverk.Fagsaksystem
import no.nav.hjelpemidler.domain.kodeverk.Fagsaktype

data class Fagsak(
    val fagsakId: String? = null,
    val fagsaksystem: Fagsaksystem? = null,
    val sakstype: Fagsaktype? = null,
)
