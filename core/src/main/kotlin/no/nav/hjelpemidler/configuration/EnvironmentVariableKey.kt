package no.nav.hjelpemidler.configuration

import kotlin.reflect.KProperty

@JvmInline
value class EnvironmentVariableKey private constructor(private val value: String) {
    constructor(key: String, prefix: String? = null, suffix: String? = null) : this(
        listOfNotNull(
            prefix,
            key,
            suffix
        ).joinToString("_")
    )

    constructor(property: KProperty<*>, prefix: String? = null, suffix: String? = null) : this(
        key = property.name.uppercase(),
        prefix = prefix,
        suffix = suffix,
    )

    override fun toString(): String = value
}
