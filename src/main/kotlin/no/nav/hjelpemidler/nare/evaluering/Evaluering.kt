package no.nav.hjelpemidler.nare.evaluering

import no.nav.hjelpemidler.nare.dokumentasjon.Metadata

data class Evaluering(
    val resultat: Resultat,
    val begrunnelse: String,
    val årsak: Årsak? = null,
    override val beskrivelse: String = "",
    override val identifikator: String = "",
    override val lovreferanse: String = "",
    override val lovdataUrl: String = "",
    val grunnlag: Map<String, String>? = emptyMap(),
    val operator: Operator = Operator.INGEN,
    var barn: List<Evaluering> = emptyList(),
) : Metadata {
    infix fun og(annen: Evaluering): Evaluering =
        Evaluering(
            resultat = resultat og annen.resultat,
            begrunnelse = "($begrunnelse OG ${annen.begrunnelse})",
            operator = Operator.OG,
            barn = this.spesifikasjonEllerBarn() + annen.spesifikasjonEllerBarn()
        )

    infix fun eller(annen: Evaluering): Evaluering =
        Evaluering(
            resultat = resultat eller annen.resultat,
            begrunnelse = "($begrunnelse ELLER ${annen.begrunnelse})",
            operator = Operator.ELLER,
            barn = this.spesifikasjonEllerBarn() + annen.spesifikasjonEllerBarn()
        )

    fun ikke(): Evaluering =
        Evaluering(
            resultat = resultat.ikke(),
            begrunnelse = "(IKKE $begrunnelse)",
            operator = Operator.IKKE,
            barn = listOf(this)
        )

    private fun spesifikasjonEllerBarn(): List<Evaluering> =
        when {
            identifikator.isBlank() && barn.isNotEmpty() -> barn
            else -> listOf(this)
        }
}
