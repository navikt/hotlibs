package no.nav.hjelpemidler.nare.spesifikasjon

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonInclude.Include
import no.nav.hjelpemidler.nare.core.Node
import no.nav.hjelpemidler.nare.evaluering.Evaluering

/**
 * @param T typen kontekst spesifikasjonen evaluerer.
 * @param E typen evaluering [evaluer] returnerer.
 * @param S spesifikk implementasjon av [Spesifikasjon].
 */
@JsonInclude(Include.NON_NULL)
abstract class Spesifikasjon<T : Any, E : Evaluering<*, E>, S : Spesifikasjon<T, E, S>> internal constructor(
    beskrivelse: String,
    id: String = "",
    barn: List<S> = emptyList(),
    @get:JsonIgnore val block: (context: T) -> E,
) : Node<S>(beskrivelse, id, barn) {
    protected abstract fun lagSpesifikasjon(
        beskrivelse: String,
        id: String = "",
        barn: List<S>,
        block: (context: T) -> E,
    ): S

    protected abstract fun onEvaluer(evaluering: E): E

    override fun og(annen: S): S =
        lagSpesifikasjon(
            beskrivelse = "($beskrivelseQuoted OG ${annen.beskrivelseQuoted})",
            barn = toList() + annen.toList(),
            block = { evaluer(it) og annen.evaluer(it) }
        )

    override fun eller(annen: S): S =
        lagSpesifikasjon(
            beskrivelse = "($beskrivelseQuoted ELLER ${annen.beskrivelseQuoted})",
            barn = toList() + annen.toList(),
            block = { evaluer(it) eller annen.evaluer(it) }
        )

    override fun ikke(): S =
        lagSpesifikasjon(
            beskrivelse = "(IKKE $beskrivelseQuoted)",
            id = "IKKE $id",
            barn = listOf(self),
            block = { evaluer(it).ikke() }
        )

    fun evaluer(context: T): E = onEvaluer(block(context))
}
