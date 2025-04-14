package no.nav.hjelpemidler.nare.regel

import no.nav.hjelpemidler.nare.spesifikasjon.Spesifikasjon

class Regel<T : Any>(
    beskrivelse: String,
    id: String = "",
    barn: List<Regel<T>> = emptyList(),
    val lovreferanse: Lovreferanse? = null,
    block: (context: T) -> Regelevaluering,
) : Spesifikasjon<T, Regelevaluering, Regel<T>>(beskrivelse, id, barn, block) {
    override val self: Regel<T> get() = this

    override fun lagSpesifikasjon(
        beskrivelse: String,
        id: String,
        barn: List<Regel<T>>,
        block: (context: T) -> Regelevaluering,
    ): Regel<T> = Regel(beskrivelse, id, barn, null, block)

    override fun onEvaluer(evaluering: Regelevaluering): Regelevaluering =
        Regelevaluering(this, evaluering)

    fun med(beskrivelse: String, id: String, lovreferanse: Lovreferanse? = null): Regel<T> =
        Regel(beskrivelse, id, barn, lovreferanse ?: this.lovreferanse, block)
}
