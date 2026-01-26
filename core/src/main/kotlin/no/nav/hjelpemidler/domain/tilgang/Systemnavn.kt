package no.nav.hjelpemidler.domain.tilgang

/**
 * e.g. "hm-oppgave-sink"
 */
class Systemnavn(value: String) : UtførtAvId(value) {
    companion object {
        val HOTSAK = Systemnavn("hm-saksbehandling")
    }
}
