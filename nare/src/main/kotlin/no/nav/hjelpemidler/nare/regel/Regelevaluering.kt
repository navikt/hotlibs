package no.nav.hjelpemidler.nare.regel

import no.nav.hjelpemidler.nare.core.Grunnlag
import no.nav.hjelpemidler.nare.core.Node
import no.nav.hjelpemidler.nare.evaluering.Operator

class Regelevaluering(
    val resultat: Regelutfall,
    val begrunnelse: String,
    val grunnlag: Grunnlag? = emptyMap(),
    val årsak: Årsak? = null,
    val operator: Operator = Operator.INGEN,
    val lovreferanse: Lovreferanse? = null,
    beskrivelse: String = "",
    id: String = "",
    barn: List<Regelevaluering> = emptyList(),
) : Node<Regelevaluering>(beskrivelse, id, barn) {
    override fun og(annen: Regelevaluering): Regelevaluering =
        Regelevaluering(
            resultat = resultat og annen.resultat,
            begrunnelse = "($begrunnelse OG ${annen.begrunnelse})",
            operator = Operator.OG,
            barn = toList() + annen.toList()
        )

    override fun eller(annen: Regelevaluering): Regelevaluering =
        Regelevaluering(
            resultat = resultat eller annen.resultat,
            begrunnelse = "($begrunnelse ELLER ${annen.begrunnelse})",
            operator = Operator.ELLER,
            barn = toList() + annen.toList()
        )

    override fun ikke(): Regelevaluering =
        Regelevaluering(
            resultat = resultat.ikke(),
            begrunnelse = "(IKKE $begrunnelse)",
            operator = Operator.IKKE,
            barn = singletonList()
        )

    override fun med(beskrivelse: String, id: String): Regelevaluering =
        med(beskrivelse, id, lovreferanse)

    fun med(beskrivelse: String, id: String, lovreferanse: Lovreferanse?): Regelevaluering =
        Regelevaluering(
            resultat = resultat,
            begrunnelse = begrunnelse,
            grunnlag = grunnlag,
            årsak = årsak,
            operator = operator,
            lovreferanse = lovreferanse,
            beskrivelse = beskrivelse,
            id = id,
            barn = barn,
        )

    override fun singletonList(): List<Regelevaluering> = listOf(this)

    override fun toString(): String = """"${super.toString()}" -> $resultat("$begrunnelse")"""

    companion object {
        fun ja(begrunnelse: String, grunnlag: Grunnlag? = emptyMap()): Regelevaluering =
            Regelevaluering(Regelutfall.JA, begrunnelse, grunnlag)

        fun nei(begrunnelse: String, grunnlag: Grunnlag? = emptyMap()): Regelevaluering =
            Regelevaluering(Regelutfall.NEI, begrunnelse, grunnlag)

        fun nei(begrunnelse: String, grunnlag: Grunnlag? = emptyMap(), årsak: Årsak): Regelevaluering =
            Regelevaluering(Regelutfall.NEI, begrunnelse, grunnlag, årsak)

        fun kanskje(begrunnelse: String): Regelevaluering =
            Regelevaluering(Regelutfall.KANSKJE, begrunnelse)
    }
}
