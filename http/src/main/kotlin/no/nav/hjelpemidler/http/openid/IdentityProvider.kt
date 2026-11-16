package no.nav.hjelpemidler.http.openid

import no.nav.hjelpemidler.configuration.Configuration

enum class IdentityProvider(
    private val value: String,
    val issuerKey: String,
) {
    /**
     * Tidligere "Azure AD".
     */
    ENTRA_ID(
        value = "azuread",
        issuerKey = "AZURE_OPENID_CONFIG_ISSUER",
    ),
    ID_PORTEN(
        value = "idporten",
        issuerKey = "IDPORTEN_ISSUER",
    ),
    MASKINPORTEN(
        value = "maskinporten",
        issuerKey = "MASKINPORTEN_ISSUER",
    ),
    TOKEN_X(
        value = "tokenx",
        issuerKey = "TOKEN_X_ISSUER",
    ),
    ;

    val issuer: String? get() = Configuration[issuerKey]

    override fun toString() = value

    companion object {
        fun forIssuer(issuer: String) = entries.firstOrNull { it.issuer == issuer }
    }
}
