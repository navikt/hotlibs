package no.nav.hjelpemidler.http.openid

import io.github.oshai.kotlinlogging.KotlinLogging
import io.ktor.client.engine.HttpClientEngine
import no.nav.hjelpemidler.service.LoadOrder
import no.nav.hjelpemidler.service.loadService
import java.util.Collections
import java.util.IdentityHashMap

private val log = KotlinLogging.logger { }

interface TexasProvider {
    operator fun invoke(engine: HttpClientEngine): Texas
}

@LoadOrder(Int.MAX_VALUE)
internal class DefaultTexasProvider : TexasProvider {
    override fun invoke(engine: HttpClientEngine): Texas {
        log.info { "Oppretter TexasClient, provider: DefaultTexasProvider, engine: ${engine.javaClass.simpleName}" }
        return TexasClient(engine)
    }
}

@LoadOrder(10)
internal class SharedTexasProvider : TexasProvider {
    override fun invoke(engine: HttpClientEngine): Texas = get(engine)

    companion object {
        private val clients: MutableMap<HttpClientEngine, Texas> = Collections.synchronizedMap(IdentityHashMap())
        operator fun get(engine: HttpClientEngine): Texas {
            log.info { "Oppretter TexasClient, provider: SharedTexasProvider, engine: ${engine.javaClass.simpleName}" }
            return clients.computeIfAbsent(engine) { TexasClient(it) }
        }
    }
}

internal val texasProvider: TexasProvider by loadService()
