package no.nav.hjelpemidler.configuration

import no.nav.hjelpemidler.text.toURI
import java.net.URI

/**
 * @see <a href="https://docs.nais.io/persistence/valkey/reference/#environment-variables">Valkey reference</a>
 */
data class ValkeyConfiguration(
    val uri: URI = DEFAULT_URI,
    val username: String? = null,
    val password: String? = null,
) {
    val host: String get() = uri.host
    val port: Int get() = uri.port
    val tls: Boolean get() = uri.scheme == "valkeys" || uri.scheme == "rediss"

    val redisUri: URI
        get() = if (tls) {
            "rediss://$host:$port"
        } else {
            "redis://$host:$port"
        }.toURI()

    constructor(instanceName: String) : this(
        uri = Configuration[key("URI", instanceName)]?.toURI() ?: DEFAULT_URI,
        username = Configuration[key("USERNAME", instanceName)],
        password = Configuration[key("PASSWORD", instanceName)],
    )

    override fun toString(): String = uri.toString()

    companion object {
        val DEFAULT_URI: URI = URI("valkey://localhost:6379")

        private fun key(key: String, instanceName: String) =
            EnvironmentVariableKey(
                key = key,
                prefix = "VALKEY",
                suffix = instanceName.uppercase(),
            )
    }
}
