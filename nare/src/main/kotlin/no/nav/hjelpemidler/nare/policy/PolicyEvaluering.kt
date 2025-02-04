package no.nav.hjelpemidler.nare.policy

import no.nav.hjelpemidler.nare.core.LogiskOperand
import no.nav.hjelpemidler.nare.core.Metadata
import no.nav.hjelpemidler.nare.evaluering.Operator

data class PolicyEvaluering(
    val resultat: PolicyResultat,
    val begrunnelse: String,
    override val beskrivelse: String = "",
    override val id: String = "",
    val operator: Operator = Operator.INGEN,
    val barn: List<PolicyEvaluering> = emptyList(),
) : Metadata, LogiskOperand<PolicyEvaluering> {
    override infix fun og(annen: PolicyEvaluering) =
        PolicyEvaluering(
            resultat = resultat og annen.resultat,
            begrunnelse = "($begrunnelse OG ${annen.begrunnelse})",
            operator = Operator.OG,
            barn = this.evalueringEllerBarn() + annen.evalueringEllerBarn()
        )

    override infix fun eller(annen: PolicyEvaluering) =
        PolicyEvaluering(
            resultat = resultat eller annen.resultat,
            begrunnelse = "($begrunnelse ELLER ${annen.begrunnelse})",
            operator = Operator.ELLER,
            barn = this.evalueringEllerBarn() + annen.evalueringEllerBarn()
        )

    override fun ikke() =
        PolicyEvaluering(
            resultat = resultat.ikke(),
            begrunnelse = "(IKKE $begrunnelse)",
            operator = Operator.IKKE,
            barn = listOf(this)
        )

    private fun evalueringEllerBarn(): List<PolicyEvaluering> =
        if (id.isBlank() && barn.isNotEmpty()) {
            barn
        } else {
            listOf(this)
        }

    companion object {
        fun tillat(begrunnelse: String = "") =
            PolicyEvaluering(PolicyResultat.TILLAT, begrunnelse)

        fun nekt(begrunnelse: String) =
            PolicyEvaluering(PolicyResultat.NEKT, begrunnelse)

        fun ikkeAktuelt(begrunnelse: String) =
            PolicyEvaluering(PolicyResultat.IKKE_AKTUELT, begrunnelse)
    }
}
