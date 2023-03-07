package no.nav.hjelpemidler.http.openid

import io.ktor.client.request.bearerAuth
import io.ktor.http.HttpMessageBuilder
import io.ktor.http.Parameters
import no.nav.hjelpemidler.cache.CacheConfiguration
import kotlin.time.Duration

fun HttpMessageBuilder.bearerAuth(tokenSet: TokenSet): Unit =
    bearerAuth(tokenSet.accessToken)

fun CacheConfiguration<Parameters, TokenSet>.tokenExpiry(leeway: Duration = TokenExpiry.LEEWAY) {
    expireAfter = TokenExpiry(leeway = leeway)
}
