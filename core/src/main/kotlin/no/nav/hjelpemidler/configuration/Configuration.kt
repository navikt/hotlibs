package no.nav.hjelpemidler.configuration

import io.github.oshai.kotlinlogging.KotlinLogging
import java.util.Properties

private val log = KotlinLogging.logger {}

/**
 * Inneholder alle miljøvariabler fra [System.getenv] i tillegg til variabler fra properties-fil for
 * gjeldende miljø (hvis denne finnes).
 *
 * NB! Variabler fra [System.getenv] overstyrer de fra properties-fil.
 *
 * @see [System.getenv]
 */
class Configuration internal constructor(
    private val properties: Map<String, String>,
) : ConfigurationMap, Map<String, String> by properties {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as Configuration
        return properties == other.properties
    }

    override fun hashCode(): Int = properties.hashCode()

    override fun toString(): String = properties.toString()

    companion object : ConfigurationMap {
        val current: Configuration by lazy(::load)

        override val size: Int get() = current.size
        override val keys: Set<String> get() = current.keys
        override val values: Collection<String> get() = current.values
        override val entries: Set<Map.Entry<String, String>> get() = current.entries

        override fun containsKey(key: String): Boolean = current.containsKey(key)
        override fun containsValue(value: String): Boolean = current.containsValue(value)
        override fun get(key: String): String? = current[key]
        override fun isEmpty(): Boolean = current.isEmpty()

        internal fun load(environment: Environment = Environment.current): Configuration {
            val location = "/$environment.properties"
            val properties = Properties()
                .apply {
                    Configuration::class.java.getResourceAsStream(location)
                        .apply {
                            if (this == null) {
                                log.info { "Leser konfigurasjon fra miljøvariabler" }
                            } else {
                                log.info { "Leser konfigurasjon fra miljøvariabler og '$location'" }
                            }
                        }?.use(::load)
                }
                .mapKeys { it.key.toString() }
                .mapValues { it.value.toString() }
            return Configuration((properties + System.getenv()).toSortedMap())
        }
    }
}
