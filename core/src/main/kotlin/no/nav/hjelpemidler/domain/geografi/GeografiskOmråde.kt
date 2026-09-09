package no.nav.hjelpemidler.domain.geografi

import no.nav.hjelpemidler.text.isInteger

sealed class GeografiskOmråde {
    abstract val id: String?

    data class Kommune(override val id: String?) : GeografiskOmråde() {
        init {
            require(id == null || (id.length == 4 && id.isInteger())) {
                "Ugyldig kommunenummer: '$id'"
            }
        }
    }

    data class Bydel(override val id: String?) : GeografiskOmråde() {
        init {
            require(id == null || (id.length == 6 && id.isInteger())) {
                "Ugyldig bydelsnummer: '$id'"
            }
        }

        val kommunenummer: String? get() = id?.take(4)
    }

    data class Land(override val id: String?) : GeografiskOmråde() {
        init {
            require(id == null || (id.length == 3 && id.isNotBlank())) {
                "Ugyldig landkode: '$id'"
            }
        }
    }
}
