package no.nav.hjelpemidler.pip.test

import io.ktor.client.engine.HttpClientEngine
import no.nav.hjelpemidler.http.openid.Texas
import no.nav.hjelpemidler.http.openid.TexasProvider
import no.nav.hjelpemidler.http.openid.TokenSet
import no.nav.hjelpemidler.http.openid.asTexas
import no.nav.hjelpemidler.service.LoadOrder
import kotlin.time.Duration.Companion.hours

@LoadOrder(Int.MIN_VALUE)
class TestTexasProvider : TexasProvider {
    override fun invoke(engine: HttpClientEngine): Texas = TokenSet("accessToken", 1.hours)
        .asTokenSetProvider()
        .asTexas()
}
