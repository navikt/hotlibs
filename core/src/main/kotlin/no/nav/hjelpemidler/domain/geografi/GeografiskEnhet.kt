package no.nav.hjelpemidler.domain.geografi

sealed class GeografiskEnhet : Comparable<GeografiskEnhet> {
    abstract val nummer: String
    abstract val navn: String

    operator fun component1(): String = nummer
    operator fun component2(): String = navn

    override fun compareTo(other: GeografiskEnhet): Int {
        val nummerComparison = nummer.compareTo(other.nummer)
        return if (nummerComparison == 0) javaClass.name.compareTo(other.javaClass.name) else nummerComparison
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as GeografiskEnhet
        return nummer == other.nummer
    }

    override fun hashCode(): Int = nummer.hashCode()

    override fun toString(): String = "$navn ($nummer)"
}
