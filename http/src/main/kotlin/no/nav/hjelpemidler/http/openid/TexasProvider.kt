package no.nav.hjelpemidler.http.openid

import io.ktor.client.engine.HttpClientEngine
import no.nav.hjelpemidler.service.LoadOrder
import no.nav.hjelpemidler.service.loadService

interface TexasProvider {
    operator fun invoke(engine: HttpClientEngine): Texas
}

@LoadOrder(Int.MAX_VALUE)
internal class DefaultTexasProvider : TexasProvider {
    override fun invoke(engine: HttpClientEngine): Texas = TexasClient(engine)
}

internal val texasProvider: TexasProvider by loadService()
