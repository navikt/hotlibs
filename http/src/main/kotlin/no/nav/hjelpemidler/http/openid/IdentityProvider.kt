package no.nav.hjelpemidler.http.openid

enum class IdentityProvider(private val value: String) {
    /**
     * Tidligere "Azure AD".
     */
    ENTRA_ID("azuread"),
    ID_PORTEN("idporten"),
    MASKINPORTEN("maskinporten"),
    TOKEN_X("tokenx"),
    ;

    override fun toString() = value
}
