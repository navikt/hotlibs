package no.nav.hjelpemidler.nare.spesifikasjon

import com.fasterxml.jackson.annotation.JsonAlias
import no.nav.hjelpemidler.nare.core.LogiskOperand

interface Spesifikasjon<in T : Any, R : LogiskOperand<R>> {
    val beskrivelse: String

    @get:JsonAlias("identifikator")
    val id: String

    fun evaluer(context: T): R
}
