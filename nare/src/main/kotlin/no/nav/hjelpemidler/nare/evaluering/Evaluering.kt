package no.nav.hjelpemidler.nare.evaluering

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonInclude.Include
import no.nav.hjelpemidler.nare.core.LogiskOperand
import no.nav.hjelpemidler.nare.core.Node
import no.nav.hjelpemidler.nare.core.singleQuoted

@JsonInclude(Include.NON_NULL)
abstract class Evaluering<R : LogiskOperand<R>, E : Evaluering<R, E>> internal constructor(
    val resultat: R,
    val begrunnelse: String,
    val operator: Operator,
    beskrivelse: String = "",
    id: String = "",
    barn: List<E> = emptyList(),
) : Node<E>(beskrivelse, id, barn) {
    private val begrunnelseQuoted: String
        @JsonIgnore
        get() = begrunnelse.singleQuoted()

    protected abstract fun lagEvaluering(
        resultat: R,
        begrunnelse: String,
        operator: Operator,
        barn: List<E>,
    ): E

    override fun og(annen: E): E =
        lagEvaluering(
            resultat = resultat og annen.resultat,
            begrunnelse = "($begrunnelseQuoted OG ${annen.begrunnelseQuoted})",
            operator = Operator.OG,
            barn = toList() + annen.toList()
        )

    override fun eller(annen: E): E =
        lagEvaluering(
            resultat = resultat eller annen.resultat,
            begrunnelse = "($begrunnelseQuoted ELLER ${annen.begrunnelseQuoted})",
            operator = Operator.ELLER,
            barn = toList() + annen.toList()
        )

    override fun ikke(): E =
        lagEvaluering(
            resultat = resultat.ikke(),
            begrunnelse = "(IKKE $begrunnelseQuoted)",
            operator = Operator.IKKE,
            barn = listOf(self)
        )
}
