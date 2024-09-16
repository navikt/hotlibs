package no.nav.hjelpemidler.nare.evaluering

class Evalueringer {
    fun ja(begrunnelse: String, grunnlag: Map<String, String>? = emptyMap()): Evaluering =
        Evaluering(Resultat.JA, begrunnelse, grunnlag = grunnlag)

    fun nei(begrunnelse: String, grunnlag: Map<String, String>? = emptyMap()): Evaluering =
        Evaluering(Resultat.NEI, begrunnelse, grunnlag = grunnlag)

    fun nei(begrunnelse: String, årsak: Årsak, grunnlag: Map<String, String>? = emptyMap()): Evaluering =
        Evaluering(Resultat.NEI, begrunnelse, årsak, grunnlag = grunnlag)

    fun kanskje(begrunnelse: String): Evaluering =
        Evaluering(Resultat.KANSKJE, begrunnelse)
}
