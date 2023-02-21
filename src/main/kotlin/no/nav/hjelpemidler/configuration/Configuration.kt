package no.nav.hjelpemidler.configuration

import org.slf4j.LoggerFactory
import java.util.Properties

private val log = LoggerFactory.getLogger(Configuration::class.java)

class Configuration internal constructor(
    private val properties: Map<String, String>,
) : Map<String, String> by properties {
    override fun equals(other: Any?): Boolean =
        properties == other

    override fun hashCode(): Int =
        properties.hashCode()

    override fun toString(): String =
        properties.toString()

    companion object {
        val current by lazy {
            load()
        }

        fun load(environment: Environment = Environment.current()): Configuration {
            val location = "/$environment.properties"
            log.info("Leser konfigurasjon fra: '$location'")
            val properties = Properties()
                .apply {
                    Configuration::class.java.getResourceAsStream(location)?.use(::load)
                }
                .mapKeys { it.key.toString() }
                .mapValues { it.value.toString() }
            return Configuration(System.getenv() + properties)
        }
    }
}
