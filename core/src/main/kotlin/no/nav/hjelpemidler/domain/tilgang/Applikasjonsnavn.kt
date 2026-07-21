package no.nav.hjelpemidler.domain.tilgang

import no.nav.hjelpemidler.configuration.NaisEnvironmentVariable

/**
 * e.g. "hm-oppgave-sink"
 */
class Applikasjonsnavn(value: String) : UtførtAvId(value) {
    companion object {
        val GJELDENDE: Applikasjonsnavn by lazy { Applikasjonsnavn(NaisEnvironmentVariable.NAIS_APP_NAME) }
    }
}
