package no.nav.hjelpemidler.configuration

import io.github.oshai.kotlinlogging.KotlinLogging
import no.nav.hjelpemidler.collections.propertiesOf
import java.util.TreeMap

private val log = KotlinLogging.logger {}

/**
 * Inneholder alle miljøvariabler fra [System.getenv] i tillegg til variabler fra properties-fil for
 * gjeldende miljø (hvis denne finnes).
 *
 * NB! Variabler fra [System.getenv] overstyrer de fra properties-fil.
 *
 * @see [System.getenv]
 * @see [EnvironmentVariable]
 * @see [EnvironmentVariableKey]
 */
object Configuration : ConfigurationMap {
    private val current: Map<String, String>

    init {
        val environment = Environment.current
        val location = "/$environment.properties"
        val properties = javaClass.getResourceAsStream(location)?.use(::propertiesOf)
        if (properties == null) {
            log.info { "Leser konfigurasjon fra miljøvariabler" }
            current = TreeMap(System.getenv())
        } else {
            log.info { "Leser konfigurasjon fra miljøvariabler og '$location'" }
            current = TreeMap()
            properties
                .map { it.key.toString() to it.value.toString() }
                .toMap(current)
                .putAll(System.getenv())
        }
    }

    override val size: Int get() = current.size
    override val keys: Set<String> get() = current.keys
    override val values: Collection<String> get() = current.values
    override val entries: Set<Map.Entry<String, String>> get() = current.entries

    override fun containsKey(key: String): Boolean = current.containsKey(key)
    override fun containsValue(value: String): Boolean = current.containsValue(value)
    override fun get(key: String): String? = current[key]
    override fun isEmpty(): Boolean = current.isEmpty()
}
