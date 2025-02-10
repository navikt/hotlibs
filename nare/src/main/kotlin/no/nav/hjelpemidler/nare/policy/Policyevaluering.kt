package no.nav.hjelpemidler.nare.policy

import no.nav.hjelpemidler.nare.core.Node
import no.nav.hjelpemidler.nare.evaluering.Operator

class Policyevaluering(
    val resultat: Policyavgjørelse,
    val begrunnelse: String,
    val operator: Operator = Operator.INGEN,
    beskrivelse: String = "",
    id: String = "",
    barn: List<Policyevaluering> = emptyList(),
) : Node<Policyevaluering>(beskrivelse, id, barn) {
    override fun og(annen: Policyevaluering): Policyevaluering =
        Policyevaluering(
            resultat = resultat og annen.resultat,
            begrunnelse = "($begrunnelse OG ${annen.begrunnelse})",
            operator = Operator.OG,
            barn = toList() + annen.toList()
        )

    override fun eller(annen: Policyevaluering): Policyevaluering =
        Policyevaluering(
            resultat = resultat eller annen.resultat,
            begrunnelse = "($begrunnelse ELLER ${annen.begrunnelse})",
            operator = Operator.ELLER,
            barn = toList() + annen.toList()
        )

    override fun ikke(): Policyevaluering =
        Policyevaluering(
            resultat = resultat.ikke(),
            begrunnelse = "(IKKE $begrunnelse)",
            operator = Operator.IKKE,
            barn = singletonList()
        )

    override fun med(beskrivelse: String, id: String): Policyevaluering =
        Policyevaluering(
            resultat = resultat,
            begrunnelse = begrunnelse,
            operator = operator,
            beskrivelse = beskrivelse,
            id = id,
            barn = barn,
        )

    override fun singletonList(): List<Policyevaluering> = listOf(this)

    override fun toString(): String = """"${super.toString()}" -> $resultat("$begrunnelse")"""

    companion object {
        fun tillat(begrunnelse: String) = Policyevaluering(Policyavgjørelse.TILLAT, begrunnelse)
        fun nekt(begrunnelse: String) = Policyevaluering(Policyavgjørelse.NEKT, begrunnelse)
        fun ikkeAktuelt(begrunnelse: String) = Policyevaluering(Policyavgjørelse.IKKE_AKTUELT, begrunnelse)
    }
}
