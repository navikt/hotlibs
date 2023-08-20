package no.nav.hjelpemidler.http.openid

import com.github.benmanes.caffeine.cache.Expiry
import io.ktor.http.Parameters

internal val ExpireImmediately: Expiry<Parameters, TokenSet> = object : Expiry<Parameters, TokenSet> {
    override fun expireAfterCreate(key: Parameters?, value: TokenSet?, currentTime: Long): Long =
        0

    override fun expireAfterUpdate(key: Parameters?, value: TokenSet?, currentTime: Long, currentDuration: Long): Long =
        0

    override fun expireAfterRead(key: Parameters?, value: TokenSet?, currentTime: Long, currentDuration: Long): Long =
        0
}
