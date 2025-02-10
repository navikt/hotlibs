package no.nav.hjelpemidler.nare.evaluering

import no.nav.hjelpemidler.nare.core.LogiskOperand

interface Evaluering<T : LogiskOperand<T>> {
    val resultat: T
    val begrunnelse: String
    val operator: Operator
}

data class DefaultEvaluering<T : LogiskOperand<T>>(
    override val resultat: T,
    override val begrunnelse: String,
    override val operator: Operator = Operator.INGEN,
) : Evaluering<T>, LogiskOperand<DefaultEvaluering<T>> {
    override fun og(annen: DefaultEvaluering<T>): DefaultEvaluering<T> = DefaultEvaluering(
        resultat = resultat og annen.resultat,
        begrunnelse = "($begrunnelse OG ${annen.begrunnelse})",
        operator = Operator.OG,
    )

    override fun eller(annen: DefaultEvaluering<T>): DefaultEvaluering<T> = DefaultEvaluering(
        resultat = resultat eller annen.resultat,
        begrunnelse = "($begrunnelse ELLER ${annen.begrunnelse})",
        operator = Operator.ELLER,
    )

    override fun ikke(): DefaultEvaluering<T> = DefaultEvaluering(
        resultat = resultat.ikke(),
        begrunnelse = "(IKKE $begrunnelse)",
        operator = Operator.IKKE,
    )

    override fun toString(): String = "$resultat(begrunnelse: '$begrunnelse')"
}
