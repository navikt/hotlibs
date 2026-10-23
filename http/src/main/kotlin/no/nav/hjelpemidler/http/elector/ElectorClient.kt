package no.nav.hjelpemidler.http.elector

import io.github.oshai.kotlinlogging.KotlinLogging
import io.ktor.client.call.body
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.request.get
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import no.nav.hjelpemidler.configuration.EnvironmentVariable
import no.nav.hjelpemidler.http.createHttpClient
import java.io.Closeable
import java.net.InetAddress

private val log = KotlinLogging.logger { }

class ElectorClient(engine: HttpClientEngine) : Closeable {
    private val client = createHttpClient(engine)
    private val hostname by lazy { InetAddress.getLocalHost().hostName }

    suspend fun isLeader(): Boolean = try {
        val response = client.get(ELECTOR_GET_URL).body<Response>()
        val hostname = withContext(Dispatchers.IO) { hostname }
        val isLeader = response.name == hostname
        log.debug { "Elector, isLeader: $isLeader, leader: ${response.name}, hostname: $hostname" }
        isLeader
    } catch (e: Exception) {
        log.error(e) { "Elector.isLeader failed" }
        throw e
    }

    override fun close() = client.close()

    private data class Response(val name: String)

    companion object {
        val ELECTOR_GET_URL by EnvironmentVariable
        val ELECTOR_SSE_URL by EnvironmentVariable
    }
}
