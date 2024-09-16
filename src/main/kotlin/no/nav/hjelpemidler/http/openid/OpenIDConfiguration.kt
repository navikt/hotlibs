package no.nav.hjelpemidler.http.openid

interface OpenIDConfiguration {
    val tokenEndpoint: String
    val clientId: String
    val clientSecret: String?
}

data class DefaultOpenIDConfiguration(
    override val tokenEndpoint: String,
    override val clientId: String,
    override val clientSecret: String?,
) : OpenIDConfiguration
