package no.nav.hjelpemidler.http.openid

import com.auth0.jwt.interfaces.DecodedJWT
import kotlin.coroutines.AbstractCoroutineContextElement
import kotlin.coroutines.CoroutineContext

class OpenIDContext(
    /**
     * `true` hvis bruker er en applikasjon og `false` hvis bruker er en person.
     */
    val userIsApplication: Boolean,

    /**
     * Token for token exchange.
     */
    val userToken: String?,
) : AbstractCoroutineContextElement(OpenIDContext) {
    constructor(userIsApplication: Boolean, userToken: DecodedJWT) : this(userIsApplication, userToken.token)

    override fun toString(): String =
        "OpenIDContext(userIsApplication=$userIsApplication, userToken=${!userToken.isNullOrBlank()})"

    companion object Key : CoroutineContext.Key<OpenIDContext>
}
