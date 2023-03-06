package no.nav.hjelpemidler.http.openid

import com.github.benmanes.caffeine.cache.Expiry
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes

class TokenExpiry<K : Any>(private val leeway: Duration = LEEWAY) : Expiry<K, TokenSet> {
    override fun expireAfterCreate(key: K, value: TokenSet, currentTime: Long): Long =
        value.expiresIn(leeway = leeway).inWholeNanoseconds

    override fun expireAfterUpdate(key: K, value: TokenSet, currentTime: Long, currentDuration: Long): Long =
        value.expiresIn(leeway = leeway).inWholeNanoseconds

    override fun expireAfterRead(key: K, value: TokenSet, currentTime: Long, currentDuration: Long): Long =
        currentDuration

    companion object {
        val LEEWAY: Duration = 1.minutes
        fun <K : Any> default(): TokenExpiry<K> =
            TokenExpiry(leeway = LEEWAY)
    }
}
