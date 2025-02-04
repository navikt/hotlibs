package no.nav.hjelpemidler.nare.policy

import com.fasterxml.jackson.annotation.JsonIgnore
import no.nav.hjelpemidler.nare.core.LogiskOperand
import no.nav.hjelpemidler.nare.core.Metadata

data class Policy<T>(
    override val beskrivelse: String,
    override val id: String = "",
    val barn: List<Policy<T>> = emptyList(),
    @JsonIgnore val implementasjon: PolicyEvaluering.Companion.(context: T) -> PolicyEvaluering,
) : Metadata, LogiskOperand<Policy<T>> {
    fun evaluer(t: T): PolicyEvaluering =
        PolicyEvaluering.implementasjon(t).copy(beskrivelse = beskrivelse, id = id)

    override infix fun og(annen: Policy<T>): Policy<T> =
        Policy(
            beskrivelse = "$beskrivelse OG ${annen.beskrivelse}",
            barn = this.policyEllerBarn() + annen.policyEllerBarn(),
            implementasjon = { evaluer(it) og annen.evaluer(it) }
        )

    override infix fun eller(annen: Policy<T>): Policy<T> =
        Policy(
            beskrivelse = "$beskrivelse ELLER ${annen.beskrivelse}",
            barn = this.policyEllerBarn() + annen.policyEllerBarn(),
            implementasjon = { evaluer(it) eller annen.evaluer(it) }
        )

    override fun ikke(): Policy<T> =
        Policy(
            beskrivelse = "IKKE $beskrivelse",
            id = "IKKE $id",
            barn = listOf(this),
            implementasjon = { evaluer(it).ikke() }
        )

    fun med(beskrivelse: String, id: String): Policy<T> =
        copy(beskrivelse = beskrivelse, id = id)

    private fun policyEllerBarn(): List<Policy<T>> =
        if (id.isBlank() && barn.isNotEmpty()) {
            barn
        } else {
            listOf(this)
        }
}
