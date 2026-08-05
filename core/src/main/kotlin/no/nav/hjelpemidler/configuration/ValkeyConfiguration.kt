package no.nav.hjelpemidler.configuration

import no.nav.hjelpemidler.text.toURI
import java.net.URI

/**
 * @see <a href="https://docs.nais.io/persistence/valkey/reference/#environment-variables">Valkey reference</a>
 */
data class ValkeyConfiguration(
    val uri: URI,
    val username: String? = null,
    val password: String? = null,
) {
    val isTls: Boolean get() = uri.scheme == "rediss" || uri.scheme == "valkeys"

    constructor(instanceName: String, provider: Provider) : this(
        uri = Configuration.get(provider.envVarPrefix, "URI", instanceName)?.toURI() ?: provider.defaultUri,
        username = Configuration.get(provider.envVarPrefix, "USERNAME", instanceName),
        password = Configuration.get(provider.envVarPrefix, "PASSWORD", instanceName),
    )

    override fun toString(): String = uri.toString()

    enum class Provider(
        val envVarPrefix: String,
        val defaultUri: URI,
    ) {
        REDIS(
            envVarPrefix = "REDIS",
            defaultUri = URI("redis://localhost:6379"),
        ),
        VALKEY(
            envVarPrefix = "VALKEY",
            defaultUri = URI("valkey://localhost:6379"),
        ),
    }
}
