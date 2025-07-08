package no.nav.hjelpemidler.configuration

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
    val tls: Boolean get() = uri.scheme == "valkeys"

    val redisUri: URI
        get() = if (tls) {
            URI("rediss://$host:$port")
        } else {
            URI("redis://$host:$port")
        }

    constructor(instanceName: String) : this(
        uri = Configuration.getOrDefault(instanceName.key("URI"), DEFAULT_URI, ::URI),
        username = Configuration[instanceName.key("USERNAME")],
        password = Configuration[instanceName.key("PASSWORD")],
    )

    override fun toString(): String = uri.toString()

    companion object {
        const val PREFIX = "VALKEY"

        val DEFAULT_URI = URI("valkey://localhost:6379")
    }
}

/**
 * Lag [EnvironmentVariableKey] med følgende format:
 * ```kotlin
 * "VALKEY_$key_$instanceName" // e.g. VALKEY_URI_CACHE
 * ```
 */
private fun String.key(key: String) =
    EnvironmentVariableKey(
        key = key,
        prefix = ValkeyConfiguration.PREFIX,
        suffix = uppercase(),
    )
