package no.nav.hjelpemidler.domain.geografi

sealed class GeografiskOmråde {
    abstract val nummer: String
    abstract val navn: String

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as GeografiskOmråde
        return nummer == other.nummer
    }

    override fun hashCode(): Int = nummer.hashCode()

    override fun toString(): String = "$navn ($nummer)"
}
