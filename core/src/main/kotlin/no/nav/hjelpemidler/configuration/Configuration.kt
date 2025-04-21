package no.nav.hjelpemidler.configuration

import io.github.oshai.kotlinlogging.KotlinLogging
import java.util.Properties

private val log = KotlinLogging.logger {}

/**
 * Inneholder alle miljøvariabler fra [System.getenv] i tillegg til variabler fra properties-fil for
 * gjeldende miljø (hvis denne finnes).
 *
 * NB! Variabler fra properties-fil overstyrer de fra [System.getenv].
 *
 * @see [System.getenv]
 */
class Configuration internal constructor(
    private val properties: Map<String, String>,
) : Map<String, String> by properties {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as Configuration
        return properties == other.properties
    }

    override fun hashCode(): Int = properties.hashCode()

    override fun toString(): String = properties.toString()

    companion object {
        val current: Configuration by lazy { load() }

        operator fun get(key: String): String? = current[key]

        fun get(key: String, prefix: String): String? = this["$prefix$key"]

        fun load(environment: Environment = Environment.current): Configuration {
            val location = "/$environment.properties"
            val properties = Properties()
                .apply {
                    Configuration::class.java.getResourceAsStream(location)
                        .apply {
                            when (this) {
                                null -> log.info { "Leser konfigurasjon fra miljøvariabler" }
                                else -> log.info { "Leser konfigurasjon fra miljøvariabler og: '$location'" }
                            }
                        }?.use(::load)
                }
                .mapKeys { it.key.toString() }
                .mapValues { it.value.toString() }
            return Configuration((System.getenv() + properties).toSortedMap())
        }
    }
}
