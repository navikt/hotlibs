package no.nav.hjelpemidler.nare.evaluering

import no.nav.hjelpemidler.nare.core.LogiskOperand
import no.nav.hjelpemidler.nare.core.MetadataLov

data class Evaluering(
    val resultat: Resultat,
    val begrunnelse: String,
    val årsak: Årsak? = null,
    override val beskrivelse: String = "",
    override val id: String = "",
    override val lovreferanse: String = "",
    override val lovdataUrl: String = "",
    val grunnlag: Map<String, String>? = emptyMap(),
    val operator: Operator = Operator.INGEN,
    val barn: List<Evaluering> = emptyList(),
) : MetadataLov, LogiskOperand<Evaluering> {
    override infix fun og(annen: Evaluering): Evaluering =
        Evaluering(
            resultat = resultat og annen.resultat,
            begrunnelse = "($begrunnelse OG ${annen.begrunnelse})",
            operator = Operator.OG,
            barn = this.evalueringEllerBarn() + annen.evalueringEllerBarn()
        )

    override infix fun eller(annen: Evaluering): Evaluering =
        Evaluering(
            resultat = resultat eller annen.resultat,
            begrunnelse = "($begrunnelse ELLER ${annen.begrunnelse})",
            operator = Operator.ELLER,
            barn = this.evalueringEllerBarn() + annen.evalueringEllerBarn()
        )

    override fun ikke(): Evaluering =
        Evaluering(
            resultat = resultat.ikke(),
            begrunnelse = "(IKKE $begrunnelse)",
            operator = Operator.IKKE,
            barn = listOf(this)
        )

    private fun evalueringEllerBarn(): List<Evaluering> =
        if (id.isBlank() && barn.isNotEmpty()) {
            barn
        } else {
            listOf(this)
        }

    companion object {
        fun ja(begrunnelse: String, grunnlag: Map<String, String>? = emptyMap()): Evaluering =
            Evaluering(Resultat.JA, begrunnelse, grunnlag = grunnlag)

        fun nei(begrunnelse: String, grunnlag: Map<String, String>? = emptyMap()): Evaluering =
            Evaluering(Resultat.NEI, begrunnelse, grunnlag = grunnlag)

        fun nei(begrunnelse: String, årsak: Årsak, grunnlag: Map<String, String>? = emptyMap()): Evaluering =
            Evaluering(Resultat.NEI, begrunnelse, årsak, grunnlag = grunnlag)

        fun kanskje(begrunnelse: String): Evaluering =
            Evaluering(Resultat.KANSKJE, begrunnelse)
    }
}
