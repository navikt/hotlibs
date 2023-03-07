package no.nav.hjelpemidler.http.openid

import com.github.benmanes.caffeine.cache.Caffeine
import io.ktor.client.request.bearerAuth
import io.ktor.http.HttpMessageBuilder
import kotlin.time.Duration

fun HttpMessageBuilder.bearerAuth(tokenSet: TokenSet): Unit =
    bearerAuth(tokenSet.accessToken)

fun Caffeine<Any, Any>.tokenExpiry(leeway: Duration = TokenExpiry.LEEWAY): Caffeine<Any, TokenSet> =
    expireAfter(TokenExpiry(leeway = leeway))
