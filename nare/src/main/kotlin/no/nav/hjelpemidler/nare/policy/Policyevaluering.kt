package no.nav.hjelpemidler.nare.policy

import com.fasterxml.jackson.annotation.JsonIgnore
import no.nav.hjelpemidler.nare.evaluering.Evaluering
import no.nav.hjelpemidler.nare.evaluering.Operator

class Policyevaluering internal constructor(
    resultat: Policyavgjørelse,
    begrunnelse: String,
    operator: Operator = Operator.INGEN,
    beskrivelse: String = "",
    id: String = "",
    barn: List<Policyevaluering> = emptyList(),
) : Evaluering<Policyavgjørelse, Policyevaluering>(resultat, begrunnelse, operator, beskrivelse, id, barn) {
    override val self: Policyevaluering get() = this

    val nekt: Boolean @JsonIgnore get() = resultat.nekt
    val tillat: Boolean @JsonIgnore get() = resultat.tillat
    val ikkeAktuelt: Boolean @JsonIgnore get() = resultat.ikkeAktuelt

    /**
     * Opprett kopi av [evaluering] med metadata fra [spesifikasjon].
     */
    internal constructor(spesifikasjon: Policy<*>, evaluering: Policyevaluering) : this(
        resultat = evaluering.resultat,
        begrunnelse = evaluering.begrunnelse,
        operator = evaluering.operator,
        beskrivelse = spesifikasjon.beskrivelse,
        id = spesifikasjon.id,
        barn = evaluering.barn,
    )

    override fun lagEvaluering(
        resultat: Policyavgjørelse,
        begrunnelse: String,
        operator: Operator,
        barn: List<Policyevaluering>,
    ): Policyevaluering =
        Policyevaluering(
            resultat = resultat,
            begrunnelse = begrunnelse,
            operator = operator,
            barn = barn,
        )

    companion object {
        fun tillat(begrunnelse: String): Policyevaluering =
            Policyevaluering(Policyavgjørelse.TILLAT, begrunnelse)

        fun nekt(begrunnelse: String): Policyevaluering =
            Policyevaluering(Policyavgjørelse.NEKT, begrunnelse)

        fun ikkeAktuelt(begrunnelse: String): Policyevaluering =
            Policyevaluering(Policyavgjørelse.IKKE_AKTUELT, begrunnelse)
    }
}
