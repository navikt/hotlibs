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
) : Map<String, String> by properties {
    operator fun get(key: EnvironmentVariableKey): String? = get(key.toString())

    fun getOrDefault(key: EnvironmentVariableKey, defaultValue: String): String =
        getOrDefault(key.toString(), defaultValue)

    operator fun contains(key: EnvironmentVariableKey): Boolean = key.toString() in this

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as Configuration
        return properties == other.properties
    }

    override fun hashCode(): Int = properties.hashCode()

    override fun toString(): String = properties.toString()

    companion object {
        val current: Configuration by lazy(::load)

        operator fun get(key: String): String? = current[key]

        operator fun get(key: EnvironmentVariableKey): String? = current[key]

        inline fun <T> get(key: String, transform: (String) -> T): T? = current[key]?.let(transform)

        inline fun <T> get(key: EnvironmentVariableKey, transform: (String) -> T): T? = current[key]?.let(transform)

        fun getOrDefault(key: String, defaultValue: String): String =
            current.getOrDefault(key, defaultValue)

        fun getOrDefault(key: EnvironmentVariableKey, defaultValue: String): String =
            current.getOrDefault(key, defaultValue)

        inline fun <T> getOrDefault(key: String, defaultValue: T, transform: (String) -> T): T =
            get(key, transform) ?: defaultValue

        inline fun <T> getOrDefault(key: EnvironmentVariableKey, defaultValue: T, transform: (String) -> T): T =
            get(key, transform) ?: defaultValue

        operator fun contains(key: String): Boolean = key in current

        operator fun contains(key: EnvironmentVariableKey): Boolean = key in current

        fun load(environment: Environment = Environment.current): Configuration {
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
