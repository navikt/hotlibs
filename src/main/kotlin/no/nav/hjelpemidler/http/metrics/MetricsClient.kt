package no.nav.hjelpemidler.http.metrics

import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.cio.CIO
import no.nav.hjelpemidler.http.createHttpClient
import no.nav.hjelpemidler.http.openid.OpenIDClient
import no.nav.hjelpemidler.http.openid.openID
import no.nav.hjelpemidler.http.openid.scopeOf
import java.time.Instant

class MetricsClient(
    private val baseUrl: String = "http://hm-metrikker.teamdigihot.svc.cluster.local",
    private val scope: String = scopeOf("hm-metrikker"),
    private val openIDClient: OpenIDClient? = null,
    engine: HttpClientEngine = CIO.create(),
) {
    private val client = createHttpClient(engine) {
        openID(scope, openIDClient)
    }

    suspend fun store(metric: Metric) {
        TODO("Ikke ferdig!")
    }

    suspend fun store(
        kilde: String,
        navn: String,
        beskrivelse: String? = null,
        vararg data: Pair<String, String>,
    ) {
        TODO("Ikke ferdig!")
    }
}

data class Metric(
    val opprettet: Instant? = null,
    val kilde: String,
    val navn: String,
    val beskrivelse: String? = null,
    val data: Any? = null,
)
