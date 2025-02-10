package no.nav.hjelpemidler.nare.regel

import com.fasterxml.jackson.annotation.JsonIgnore
import no.nav.hjelpemidler.nare.core.Node
import no.nav.hjelpemidler.nare.spesifikasjon.Spesifikasjon

class Regel<T : Any>(
    beskrivelse: String,
    id: String = "",
    barn: List<Regel<T>> = emptyList(),
    val lovreferanse: Lovreferanse? = null,
    @get:JsonIgnore val implementasjon: Regelevaluering.Companion.(kontekst: T) -> Regelevaluering,
) : Node<Regel<T>>(beskrivelse, id, barn), Spesifikasjon<T, Regelevaluering> {
    override fun evaluer(kontekst: T): Regelevaluering =
        Regelevaluering
            .implementasjon(kontekst)
            .med(beskrivelse, id, lovreferanse)

    override fun og(annen: Regel<T>): Regel<T> =
        Regel(
            beskrivelse = "$beskrivelse OG ${annen.beskrivelse}",
            barn = toList() + annen.toList(),
            implementasjon = { evaluer(it) og annen.evaluer(it) }
        )

    override fun eller(annen: Regel<T>): Regel<T> =
        Regel(
            beskrivelse = "$beskrivelse ELLER ${annen.beskrivelse}",
            barn = toList() + annen.toList(),
            implementasjon = { evaluer(it) eller annen.evaluer(it) }
        )

    override fun ikke(): Regel<T> =
        Regel(
            beskrivelse = "IKKE $beskrivelse",
            id = "IKKE $id",
            barn = singletonList(),
            implementasjon = { evaluer(it).ikke() }
        )

    override fun med(beskrivelse: String, id: String): Regel<T> =
        med(beskrivelse, id, lovreferanse)

    fun med(beskrivelse: String, id: String, lovreferanse: Lovreferanse?): Regel<T> =
        Regel(
            beskrivelse = beskrivelse,
            id = id,
            barn = barn,
            lovreferanse = lovreferanse,
            implementasjon = implementasjon,
        )

    override fun singletonList(): List<Regel<T>> = listOf(this)
}
