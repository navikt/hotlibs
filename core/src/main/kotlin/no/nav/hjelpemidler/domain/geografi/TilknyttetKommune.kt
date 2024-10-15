package no.nav.hjelpemidler.domain.geografi

interface TilknyttetKommune {
    val kommune: Kommune?
    val kommunenummer: String? get() = kommune?.nummer
}
