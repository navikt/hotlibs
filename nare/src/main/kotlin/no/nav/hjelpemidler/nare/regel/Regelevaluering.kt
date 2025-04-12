package no.nav.hjelpemidler.nare.regel

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonInclude.Include
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import no.nav.hjelpemidler.nare.core.Grunnlag
import no.nav.hjelpemidler.nare.core.Node
import no.nav.hjelpemidler.nare.evaluering.Evaluering
import no.nav.hjelpemidler.nare.evaluering.Operator

@JsonDeserialize(using = RegelevalueringDeserializer::class)
class Regelevaluering internal constructor(
    override val resultat: Regelutfall,
    override val begrunnelse: String,
    override val operator: Operator = Operator.INGEN,
    @get:JsonInclude(Include.NON_EMPTY)
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

    val ja: Boolean @JsonIgnore get() = resultat.ja
    val nei: Boolean @JsonIgnore get() = resultat.nei
    val kanskje: Boolean @JsonIgnore get() = resultat.kanskje

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
