package no.nav.hjelpemidler.serialization.kotlinx

abstract class ToStringSerializer<T>(
    serialName: String,
    private val creator: (rawValue: String) -> T,
) : AbstractStringSerializer<T>(serialName) {
    override fun serialize(value: T): String = value.toString()
    override fun deserialize(value: String): T = creator(value)
}
