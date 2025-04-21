package no.nav.hjelpemidler.domain.enhet

abstract class AbstractEnhet {
    abstract val nummer: String
    abstract val navn: String

    operator fun component1(): String = nummer
    operator fun component2(): String = navn

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as AbstractEnhet
        return nummer == other.nummer
    }

    override fun hashCode(): Int = nummer.hashCode()

    override fun toString(): String = "$navn ($nummer)"
}
