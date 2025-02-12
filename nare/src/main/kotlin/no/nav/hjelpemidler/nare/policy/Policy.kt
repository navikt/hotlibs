package no.nav.hjelpemidler.nare.policy

import com.fasterxml.jackson.annotation.JsonIgnore
import no.nav.hjelpemidler.nare.core.Node
import no.nav.hjelpemidler.nare.spesifikasjon.Spesifikasjon

class Policy<T : Any>(
    beskrivelse: String,
    id: String = "",
    barn: List<Policy<T>> = emptyList(),
    @get:JsonIgnore val implementasjon: Policyevaluering.Companion.(context: T) -> Policyevaluering,
) : Node<Policy<T>>(beskrivelse, id, barn), Spesifikasjon<T, Policyevaluering> {
    override fun evaluer(context: T): Policyevaluering =
        Policyevaluering
            .implementasjon(context)
            .med(beskrivelse, id)

    override fun og(annen: Policy<T>): Policy<T> =
        Policy(
            beskrivelse = "$beskrivelse OG ${annen.beskrivelse}",
            barn = toList() + annen.toList(),
            implementasjon = { evaluer(it) og annen.evaluer(it) }
        )

    override fun eller(annen: Policy<T>): Policy<T> =
        Policy(
            beskrivelse = "$beskrivelse ELLER ${annen.beskrivelse}",
            barn = toList() + annen.toList(),
            implementasjon = { evaluer(it) eller annen.evaluer(it) }
        )

    override fun ikke(): Policy<T> =
        Policy(
            beskrivelse = "IKKE $beskrivelse",
            id = "IKKE $id",
            barn = listOf(this),
            implementasjon = { evaluer(it).ikke() }
        )

    override fun med(beskrivelse: String, id: String): Policy<T> =
        Policy(
            beskrivelse = beskrivelse,
            id = id,
            barn = barn,
            implementasjon = implementasjon
        )
}
