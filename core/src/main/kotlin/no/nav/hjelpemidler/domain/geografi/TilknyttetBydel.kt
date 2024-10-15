package no.nav.hjelpemidler.domain.geografi

interface TilknyttetBydel {
    val bydel: Bydel?
    val bydelsnummer: String? get() = bydel?.nummer
}
