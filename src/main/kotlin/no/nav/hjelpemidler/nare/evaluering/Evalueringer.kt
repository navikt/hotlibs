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
        lovReferanse: String,
        lovdataLenke: String,
        evaluering: Evaluering,
    ): Evaluering =
        evaluering.copy(
            identifikator = identifikator,
            beskrivelse = beskrivelse,
            lovReferanse = lovReferanse,
            lovdataLenke = lovdataLenke
        )
}
