package no.nav.hjelpemidler.domain.enhet

abstract class AbstractEnhet<T : Comparable<T>> : Comparable<AbstractEnhet<T>> {
    abstract val nummer: T
    abstract val navn: String

    operator fun component1(): T = nummer
    operator fun component2(): String = navn

    override fun compareTo(other: AbstractEnhet<T>): Int {
        val nummerComparison = nummer.compareTo(other.nummer)
        return if (nummerComparison == 0) javaClass.name.compareTo(other.javaClass.name) else nummerComparison
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as AbstractEnhet<*>
        return nummer == other.nummer
    }

    override fun hashCode(): Int = 31 * javaClass.hashCode() + nummer.hashCode()

    override fun toString(): String = "$navn ($nummer)"
}
