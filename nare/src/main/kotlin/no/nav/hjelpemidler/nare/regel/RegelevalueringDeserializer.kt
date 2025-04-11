package no.nav.hjelpemidler.nare.regel

import com.fasterxml.jackson.annotation.JsonAlias
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.deser.std.StdNodeBasedDeserializer
import com.fasterxml.jackson.databind.node.ObjectNode
import no.nav.hjelpemidler.nare.core.Grunnlag
import no.nav.hjelpemidler.nare.evaluering.Operator

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
}
