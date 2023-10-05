package no.nav.hjelpemidler.nare.spesifikasjon

import com.fasterxml.jackson.annotation.JsonIgnore
import no.nav.hjelpemidler.nare.dokumentasjon.Metadata
import no.nav.hjelpemidler.nare.evaluering.Evaluering
import no.nav.hjelpemidler.nare.evaluering.Evalueringer

data class Spesifikasjon<T>(
    override val beskrivelse: String,
    override val identifikator: String = "",
    override val lovreferanse: String = "",
    override val lovdataUrl: String = "",
    val grunnlag: Map<String, String> = emptyMap(),
    val barn: List<Spesifikasjon<T>> = emptyList(),
    @JsonIgnore val implementasjon: Evalueringer.(T) -> Evaluering,
) : Metadata {
    fun evaluer(t: T): Evaluering =
        Evalueringer().run {
            evaluer(
                beskrivelse = beskrivelse,
                identifikator = identifikator,
                lovreferanse = lovreferanse,
                lovdataUrl = lovdataUrl,
                evaluering = implementasjon(this, t)
            )
        }

    infix fun og(annen: Spesifikasjon<T>): Spesifikasjon<T> =
        Spesifikasjon(
            beskrivelse = "$beskrivelse OG ${annen.beskrivelse}",
            barn = this.spesifikasjonEllerBarn() + annen.spesifikasjonEllerBarn(),
            implementasjon = { evaluer(it) og annen.evaluer(it) }
        )

    infix fun eller(annen: Spesifikasjon<T>): Spesifikasjon<T> =
        Spesifikasjon(
            beskrivelse = "$beskrivelse ELLER ${annen.beskrivelse}",
            barn = this.spesifikasjonEllerBarn() + annen.spesifikasjonEllerBarn(),
            implementasjon = { evaluer(it) eller annen.evaluer(it) }
        )

    fun ikke(): Spesifikasjon<T> =
        Spesifikasjon(
            beskrivelse = "IKKE $beskrivelse",
            identifikator = "IKKE $identifikator",
            barn = listOf(this),
            implementasjon = { evaluer(it).ikke() }
        )

    fun med(identifikator: String, beskrivelse: String): Spesifikasjon<T> =
        copy(identifikator = identifikator, beskrivelse = beskrivelse)

    private fun spesifikasjonEllerBarn(): List<Spesifikasjon<T>> =
        when {
            identifikator.isBlank() && barn.isNotEmpty() -> barn
            else -> listOf(this)
        }
}

fun <T> ikke(spesifikasjon: Spesifikasjon<T>): Spesifikasjon<T> =
    spesifikasjon.ikke()
