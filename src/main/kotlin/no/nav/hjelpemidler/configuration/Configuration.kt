package no.nav.hjelpemidler.configuration

import java.util.Properties

class Configuration private constructor(
    private val properties: Map<String, String>,
) : Map<String, String> by properties {
    constructor(properties: Properties) : this(
        properties = properties
            .mapKeys {
                it.key.toString()
            }
            .mapValues {
                it.value.toString()
            }
    )

    constructor(location: String) : this(
        properties = Properties().apply {
            Configuration::class.java.getResourceAsStream(location)?.use(::load)
        }
    )

    override fun equals(other: Any?): Boolean =
        properties == other

    override fun hashCode(): Int =
        properties.hashCode()

    override fun toString(): String =
        properties.toString()

    companion object {
        fun load(location: String): Lazy<Configuration> = lazy {
            Configuration(location)
        }

        fun load(environment: Environment = Environment.current()): Lazy<Configuration> =
            load("/$environment.properties")
    }
}
