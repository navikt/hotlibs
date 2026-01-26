package no.nav.hjelpemidler.domain.tilgang

/**
 * Nav-ident med følgende format: `A123456`
 */
class NavIdent(value: String) : UtførtAvId(value) {
    init {
        if (!erGyldig(value)) {
            throw IllegalArgumentException("Ugyldig Nav-ident: '$value'")
        }
    }

    companion object {
        private val regex: Regex = Regex("^[A-Z][0-9]{6}$")

        fun erGyldig(value: String): Boolean = value.matches(regex)

        val UKJENT = NavIdent("Z999999")
    }
}
