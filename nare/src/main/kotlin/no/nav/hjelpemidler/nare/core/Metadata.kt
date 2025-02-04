package no.nav.hjelpemidler.nare.core

import com.fasterxml.jackson.annotation.JsonAlias

interface Metadata {
    val beskrivelse: String

    @get:JsonAlias("identifikator")
    val id: String
}

interface MetadataLov : Metadata {
    @get:JsonAlias("lovReferanse")
    val lovreferanse: String

    @get:JsonAlias("lovdataLenke")
    val lovdataUrl: String
}
