package no.nav.hjelpemidler.nare.dokumentasjon

data class DefaultMetadata(
    override val beskrivelse: String,
    override val identifikator: String,
    override val lovreferanse: String,
    override val lovdataUrl: String,
) : Metadata
