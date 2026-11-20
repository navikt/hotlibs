package no.nav.hjelpemidler.security

/**
 * @see <a href="https://docs.digdir.no/docs/idporten/oidc/oidc_protocol_id_token#acr-values">id_token from ID-porten | ID-porten</a>
 */
enum class IdPortenSecurityLevel(val acrValue: String, val relativeValue: Int) {
    /** Ikke i bruk pt. */
    LOW(acrValue = "idporten-loa-low", relativeValue = 1),

    /** Tidligere `Level3` */
    SUBSTANTIAL(acrValue = "idporten-loa-substantial", relativeValue = 2),

    /** Tidligere `Level4` */
    HIGH(acrValue = "idporten-loa-high", relativeValue = 3),
    ;

    override fun toString(): String = acrValue

    companion object {
        internal const val LEVEL_3 = "Level3"
        internal const val LEVEL_4 = "Level4"

        fun of(acrValue: String): IdPortenSecurityLevel = when (acrValue) {
            LEVEL_3 -> SUBSTANTIAL
            LEVEL_4 -> HIGH
            else -> entries.firstOrNull { it.acrValue == acrValue }
                ?: throw IllegalArgumentException("Ukjent acrValue: '$acrValue'")
        }
    }
}
