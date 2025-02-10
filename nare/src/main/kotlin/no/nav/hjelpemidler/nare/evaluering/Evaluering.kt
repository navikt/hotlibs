package no.nav.hjelpemidler.nare.evaluering

import no.nav.hjelpemidler.nare.core.LogiskOperand

data class Evaluering<T : LogiskOperand<T>>(
    val resultat: T,
    val begrunnelse: String,
    val operator: Operator = Operator.INGEN,
) : LogiskOperand<Evaluering<T>> {
    override fun og(annen: Evaluering<T>): Evaluering<T> = Evaluering(
        resultat = resultat og annen.resultat,
        begrunnelse = "($begrunnelse OG ${annen.begrunnelse})",
        operator = Operator.OG,
    )

    override fun eller(annen: Evaluering<T>): Evaluering<T> = Evaluering(
        resultat = resultat eller annen.resultat,
        begrunnelse = "($begrunnelse ELLER ${annen.begrunnelse})",
        operator = Operator.ELLER,
    )

    override fun ikke(): Evaluering<T> = Evaluering(
        resultat = resultat.ikke(),
        begrunnelse = "(IKKE $begrunnelse)",
        operator = Operator.IKKE,
    )

    override fun toString(): String = """$resultat(begrunnelse: "$begrunnelse")"""
}
