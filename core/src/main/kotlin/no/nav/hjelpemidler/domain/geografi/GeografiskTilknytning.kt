package no.nav.hjelpemidler.domain.geografi

sealed interface GeografiskTilknytning

sealed class GeografiskOmråde : GeografiskTilknytning {
    abstract val nummer: String
    abstract val navn: String

    override fun toString(): String = "$navn ($nummer)"
}
