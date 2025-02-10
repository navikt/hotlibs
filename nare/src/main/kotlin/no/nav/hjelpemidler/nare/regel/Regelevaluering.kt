package no.nav.hjelpemidler.nare.regel

import no.nav.hjelpemidler.nare.core.Grunnlag
import no.nav.hjelpemidler.nare.core.Node
import no.nav.hjelpemidler.nare.evaluering.Evaluering
import no.nav.hjelpemidler.nare.evaluering.Operator

class Regelevaluering(
    override val resultat: Regelutfall,
    override val begrunnelse: String,
    override val operator: Operator = Operator.INGEN,
    val grunnlag: Grunnlag? = emptyMap(),
    val årsak: Årsak? = null,
    val lovreferanse: Lovreferanse? = null,
    beskrivelse: String = "",
    id: String = "",
    barn: List<Regelevaluering> = emptyList(),
) : Node<Regelevaluering>(beskrivelse, id, barn), Evaluering<Regelutfall> {
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
            barn = listOf(this)
        )

    override fun med(beskrivelse: String, id: String): Regelevaluering =
        med(beskrivelse, id, lovreferanse)

    fun med(beskrivelse: String, id: String, lovreferanse: Lovreferanse?): Regelevaluering =
        Regelevaluering(
            resultat = resultat,
            begrunnelse = begrunnelse,
            operator = operator,
            grunnlag = grunnlag,
            årsak = årsak,
            lovreferanse = lovreferanse,
            beskrivelse = beskrivelse,
            id = id,
            barn = barn,
        )

    override fun toString(): String = "${super.toString()} -> $resultat(begrunnelse: '$begrunnelse')"

    companion object {
        fun ja(begrunnelse: String, grunnlag: Grunnlag? = emptyMap()): Regelevaluering =
            Regelevaluering(Regelutfall.JA, begrunnelse, grunnlag = grunnlag)

        fun nei(begrunnelse: String, grunnlag: Grunnlag? = emptyMap()): Regelevaluering =
            Regelevaluering(Regelutfall.NEI, begrunnelse, grunnlag = grunnlag)

        fun nei(begrunnelse: String, grunnlag: Grunnlag? = emptyMap(), årsak: Årsak): Regelevaluering =
            Regelevaluering(Regelutfall.NEI, begrunnelse, grunnlag = grunnlag, årsak = årsak)

        fun kanskje(begrunnelse: String): Regelevaluering =
            Regelevaluering(Regelutfall.KANSKJE, begrunnelse)
    }
}
