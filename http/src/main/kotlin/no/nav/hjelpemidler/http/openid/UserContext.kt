package no.nav.hjelpemidler.http.openid

import com.auth0.jwt.interfaces.DecodedJWT
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.withContext
import kotlin.coroutines.AbstractCoroutineContextElement
import kotlin.coroutines.CoroutineContext

/**
 * Holder på [Token] for gjeldende bruker.
 *
 * @see [TexasTokenSetProvider]
 */
class UserContext(val userToken: Token) : AbstractCoroutineContextElement(UserContext) {
    constructor(userToken: DecodedJWT) : this(Token(userToken))

    companion object Key : CoroutineContext.Key<UserContext>
}

/**
 * @see [TexasTokenSetProvider]
 */
suspend fun <T> withUserContext(userToken: String, block: suspend CoroutineScope.() -> T): T =
    withContext(UserContext(Token(userToken)), block)

/**
 * Hent gjeldende [UserContext] hvis denne er satt.
 */
fun CoroutineContext.userContext(): UserContext? = this[UserContext]

/**
 * Hent gjeldende [UserContext] hvis denne er satt.
 */
suspend fun currentUserContext(): UserContext? = currentCoroutineContext().userContext()
