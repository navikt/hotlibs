package no.nav.hjelpemidler.domain.joark

data class EndretDokument(
    val dokumentId: String,
    val tittel: String,
    val annetInnhold: Set<String> = emptySet(),
)
