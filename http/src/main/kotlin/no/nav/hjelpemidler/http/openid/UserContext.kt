package no.nav.hjelpemidler.http.openid

import com.auth0.jwt.interfaces.DecodedJWT
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.withContext
import kotlin.coroutines.AbstractCoroutineContextElement
import kotlin.coroutines.CoroutineContext

/**
 * Holder på [userToken] for gjeldende bruker.
 */
class UserContext(val userToken: String) : AbstractCoroutineContextElement(UserContext) {
    constructor(userToken: DecodedJWT) : this(userToken.token)

    companion object Key : CoroutineContext.Key<UserContext>
}

/**
 * Set [userToken] som benyttes til å bestemme om det skal gjøres token exchange eller kun hentes token (med mindre
 * [TokenExchangePreventionToken] er satt).
 *
 * @see [TexasTokenSetProvider]
 */
suspend fun <T> withUserContext(userToken: String, block: suspend CoroutineScope.() -> T): T =
    withContext(UserContext(userToken), block)

/**
 * Hent gjeldende [UserContext] hvis denne er satt.
 */
fun CoroutineContext.userContext(): UserContext? = this[UserContext]

/**
 * Hent gjeldende [UserContext] hvis denne er satt.
 */
suspend fun currentUserContext(): UserContext? = currentCoroutineContext().userContext()
