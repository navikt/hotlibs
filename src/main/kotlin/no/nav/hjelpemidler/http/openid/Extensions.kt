package no.nav.hjelpemidler.http.openid

import com.github.benmanes.caffeine.cache.Caffeine
import io.ktor.client.request.bearerAuth
import io.ktor.http.HttpMessageBuilder
import io.ktor.http.Parameters
import kotlin.time.Duration

fun HttpMessageBuilder.bearerAuth(tokenSet: TokenSet): Unit =
    bearerAuth(tokenSet.accessToken)

fun Caffeine<Any, Any>.untilTokenExpiry(leeway: Duration = TokenExpiry.LEEWAY): Caffeine<Any, TokenSet> =
    expireAfter(TokenExpiry(leeway = leeway))

internal val Parameters.grantType
    get() = get("grant_type")

internal val Parameters.scope
    get() = get("scope")
