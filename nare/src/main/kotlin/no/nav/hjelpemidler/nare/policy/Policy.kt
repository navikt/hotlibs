package no.nav.hjelpemidler.nare.policy

import no.nav.hjelpemidler.nare.spesifikasjon.Spesifikasjon

class Policy<T : Any>(
    beskrivelse: String,
    id: String = "",
    barn: List<Policy<T>> = emptyList(),
    block: (context: T) -> Policyevaluering,
) : Spesifikasjon<T, Policyevaluering, Policy<T>>(beskrivelse, id, barn, block) {
    override val self: Policy<T> get() = this

    override fun lagSpesifikasjon(
        beskrivelse: String,
        id: String,
        barn: List<Policy<T>>,
        block: (context: T) -> Policyevaluering,
    ): Policy<T> = Policy(beskrivelse, id, barn, block)

    override fun onEvaluer(evaluering: Policyevaluering): Policyevaluering =
        Policyevaluering(this, evaluering)

    fun med(beskrivelse: String, id: String): Policy<T> =
        Policy(beskrivelse, id, barn, block)
}
