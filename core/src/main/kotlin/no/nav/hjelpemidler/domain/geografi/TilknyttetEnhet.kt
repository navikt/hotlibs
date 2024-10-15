package no.nav.hjelpemidler.domain.geografi

interface TilknyttetEnhet {
    val enhet: Enhet
    val enhetsnummer: String get() = enhet.nummer
}
