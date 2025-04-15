package no.nav.hjelpemidler.nare.regel

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonInclude.Include
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import no.nav.hjelpemidler.nare.core.Grunnlag
import no.nav.hjelpemidler.nare.evaluering.Evaluering
import no.nav.hjelpemidler.nare.evaluering.Operator

@JsonDeserialize(using = RegelevalueringDeserializer::class)
class Regelevaluering internal constructor(
    resultat: Regelutfall,
    begrunnelse: String,
    operator: Operator = Operator.INGEN,
    @get:JsonInclude(Include.NON_EMPTY)
    val grunnlag: Grunnlag? = emptyMap(),
    val årsak: Årsak? = null,
    val lovreferanse: Lovreferanse? = null,
    beskrivelse: String = "",
    id: String = "",
    barn: List<Regelevaluering> = emptyList(),
) : Evaluering<Regelutfall, Regelevaluering>(resultat, begrunnelse, operator, beskrivelse, id, barn) {
    override val self: Regelevaluering get() = this

    val ja: Boolean @JsonIgnore get() = resultat.ja
    val nei: Boolean @JsonIgnore get() = resultat.nei
    val kanskje: Boolean @JsonIgnore get() = resultat.kanskje

    /**
     * Opprett kopi av [evaluering] med metadata fra [spesifikasjon].
     */
    internal constructor(spesifikasjon: Regel<*>, evaluering: Regelevaluering) : this(
        resultat = evaluering.resultat,
        begrunnelse = evaluering.begrunnelse,
        operator = evaluering.operator,
        grunnlag = evaluering.grunnlag,
        årsak = evaluering.årsak,
        lovreferanse = spesifikasjon.lovreferanse,
        beskrivelse = spesifikasjon.beskrivelse,
        id = spesifikasjon.id,
        barn = evaluering.barn,
    )

    override fun lagEvaluering(
        resultat: Regelutfall,
        begrunnelse: String,
        operator: Operator,
        barn: List<Regelevaluering>,
    ): Regelevaluering =
        Regelevaluering(
            resultat = resultat,
            begrunnelse = begrunnelse,
            operator = operator,
            barn = barn,
        )

    companion object {
        fun ja(begrunnelse: String, grunnlag: Grunnlag? = emptyMap(), årsak: Årsak? = null): Regelevaluering =
            Regelevaluering(Regelutfall.JA, begrunnelse, grunnlag = grunnlag, årsak = årsak)

        fun nei(begrunnelse: String, grunnlag: Grunnlag? = emptyMap(), årsak: Årsak? = null): Regelevaluering =
            Regelevaluering(Regelutfall.NEI, begrunnelse, grunnlag = grunnlag, årsak = årsak)

        fun kanskje(begrunnelse: String, grunnlag: Grunnlag? = emptyMap(), årsak: Årsak? = null): Regelevaluering =
            Regelevaluering(Regelutfall.KANSKJE, begrunnelse, grunnlag = grunnlag, årsak = årsak)
    }
}
