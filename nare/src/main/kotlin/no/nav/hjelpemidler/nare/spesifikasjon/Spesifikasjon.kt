package no.nav.hjelpemidler.nare.spesifikasjon

import com.fasterxml.jackson.annotation.JsonIgnore
import no.nav.hjelpemidler.nare.core.LogiskOperand
import no.nav.hjelpemidler.nare.core.MetadataLov
import no.nav.hjelpemidler.nare.evaluering.Evaluering

data class Spesifikasjon<T>(
    override val beskrivelse: String,
    override val id: String = "",
    override val lovreferanse: String = "",
    override val lovdataUrl: String = "",
    val grunnlag: Map<String, String> = emptyMap(),
    val barn: List<Spesifikasjon<T>> = emptyList(),
    @JsonIgnore val implementasjon: Evaluering.Companion.(context: T) -> Evaluering,
) : MetadataLov, LogiskOperand<Spesifikasjon<T>> {
    fun evaluer(t: T): Evaluering =
        Evaluering.implementasjon(t)
            .copy(
                beskrivelse = beskrivelse,
                id = id,
                lovreferanse = lovreferanse,
                lovdataUrl = lovdataUrl
            )

    override infix fun og(annen: Spesifikasjon<T>): Spesifikasjon<T> =
        Spesifikasjon(
            beskrivelse = "$beskrivelse OG ${annen.beskrivelse}",
            barn = this.spesifikasjonEllerBarn() + annen.spesifikasjonEllerBarn(),
            implementasjon = { evaluer(it) og annen.evaluer(it) }
        )

    override infix fun eller(annen: Spesifikasjon<T>): Spesifikasjon<T> =
        Spesifikasjon(
            beskrivelse = "$beskrivelse ELLER ${annen.beskrivelse}",
            barn = this.spesifikasjonEllerBarn() + annen.spesifikasjonEllerBarn(),
            implementasjon = { evaluer(it) eller annen.evaluer(it) }
        )

    override fun ikke(): Spesifikasjon<T> =
        Spesifikasjon(
            beskrivelse = "IKKE $beskrivelse",
            id = "IKKE $id",
            barn = listOf(this),
            implementasjon = { evaluer(it).ikke() }
        )

    fun med(beskrivelse: String, id: String): Spesifikasjon<T> =
        copy(beskrivelse = beskrivelse, id = id)

    private fun spesifikasjonEllerBarn(): List<Spesifikasjon<T>> =
        if (id.isBlank() && barn.isNotEmpty()) {
            barn
        } else {
            listOf(this)
        }
}
