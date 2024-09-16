package no.nav.hjelpemidler.nare.dokumentasjon

import com.fasterxml.jackson.annotation.JsonProperty

interface Metadata {
    val beskrivelse: String

    val identifikator: String

    @get:JsonProperty("lovReferanse")
    val lovreferanse: String

    @get:JsonProperty("lovdataLenke")
    val lovdataUrl: String
}
