package no.nav.hjelpemidler.domain.artikkel

interface Artikkel {
    val hmsArtNr: String
    val artikkelnavn: String
}

interface Artikkellinje : Artikkel {
    /**
     * ID for hjelpemiddel/tilbehør i behovsmeldingen. Brukes bla. til å identifisere hvilket hjelpemiddel/tilbehør
     * saksbehandlere ønsker å endre ifm. saksbehandling.
     */
    val id: String
    val antall: Int
}
