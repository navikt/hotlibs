package no.nav.hjelpemidler.nare.evaluering

class Evalueringer {
    fun ja(begrunnelse: String, grunnlag: Map<String, String>? = emptyMap()): Evaluering =
        Evaluering(Resultat.JA, begrunnelse, grunnlag = grunnlag)

    fun nei(begrunnelse: String, grunnlag: Map<String, String>? = emptyMap()): Evaluering =
        Evaluering(Resultat.NEI, begrunnelse, grunnlag = grunnlag)

    fun kanskje(begrunnelse: String): Evaluering =
        Evaluering(Resultat.KANSKJE, begrunnelse)

    fun evaluer(
        identifikator: String,
        beskrivelse: String,
        lovreferanse: String,
        lovdataUrl: String,
        evaluering: Evaluering,
    ): Evaluering =
        evaluering.copy(
            identifikator = identifikator,
            beskrivelse = beskrivelse,
            lovreferanse = lovreferanse,
            lovdataUrl = lovdataUrl
        )
}
