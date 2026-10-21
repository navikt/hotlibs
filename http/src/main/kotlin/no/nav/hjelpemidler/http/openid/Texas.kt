package no.nav.hjelpemidler.http.openid

interface Texas {
    suspend fun token(identityProvider: IdentityProvider, target: String): TokenSet
    suspend fun exchange(identityProvider: IdentityProvider, target: String, userToken: String): TokenSet
    suspend fun introspection(identityProvider: IdentityProvider, token: String): TokenIntrospection

    /**
     * [TokenSetProvider] for [IdentityProvider.ENTRA_ID].
     */
    fun entraID(defaultTarget: String): TokenSetProvider = EntraIDTokenSetProvider(
        texas = this,
        defaultTarget = defaultTarget,
    )

    /**
     * [TokenSetProvider] for [IdentityProvider.MASKINPORTEN].
     */
    fun maskinporten(defaultTarget: String): TokenSetProvider = ApplicationTokenSetProvider(
        texas = this,
        identityProvider = IdentityProvider.MASKINPORTEN,
        defaultTarget = defaultTarget,
    )

    /**
     * [TokenSetProvider] for [IdentityProvider.TOKEN_X].
     */
    fun tokenX(defaultTarget: String): TokenSetProvider = UserTokenSetProvider(
        texas = this,
        identityProvider = IdentityProvider.TOKEN_X,
        defaultTarget = defaultTarget,
    )
}
