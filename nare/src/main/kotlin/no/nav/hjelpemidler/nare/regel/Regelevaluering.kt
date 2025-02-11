package no.nav.hjelpemidler.nare.regel

import com.fasterxml.jackson.annotation.JsonAlias
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonInclude.Include
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.deser.std.StdNodeBasedDeserializer
import com.fasterxml.jackson.databind.node.ObjectNode
import no.nav.hjelpemidler.nare.core.Grunnlag
import no.nav.hjelpemidler.nare.core.Node
import no.nav.hjelpemidler.nare.evaluering.Evaluering
import no.nav.hjelpemidler.nare.evaluering.Operator

@JsonDeserialize(using = RegelevalueringDeserializer::class)
class Regelevaluering(
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

internal class RegelevalueringDeserializer : StdNodeBasedDeserializer<Regelevaluering>(Regelevaluering::class.java) {
    override fun convert(root: JsonNode, context: DeserializationContext): Regelevaluering? {
        if (root !is ObjectNode) return null

        val referanse = root["lovReferanse"]?.textValue() ?: ""
        val url = root["lovdataLenke"]?.textValue() ?: ""
        val lovreferanse = if (referanse.isBlank() && url.isBlank()) {
            null
        } else {
            Lovreferanse(referanse, url)
        }

        val adapter = context.readTreeAsValue(root, RegelevalueringAdapter::class.java)
        return Regelevaluering(
            resultat = adapter.resultat,
            begrunnelse = adapter.begrunnelse,
            operator = adapter.operator,
            grunnlag = adapter.grunnlag,
            årsak = adapter.årsak,
            lovreferanse = adapter.lovreferanse ?: lovreferanse,
            beskrivelse = adapter.beskrivelse,
            id = adapter.id,
            barn = adapter.barn ?: emptyList(),
        )
    }
}

private data class RegelevalueringAdapter(
    val resultat: Regelutfall,
    val begrunnelse: String,
    val operator: Operator,
    val grunnlag: Grunnlag?,
    val årsak: Årsak?,
    val lovreferanse: Lovreferanse?,
    val beskrivelse: String,
    @JsonAlias("identifikator")
    val id: String,
    val barn: List<Regelevaluering>?,
)
