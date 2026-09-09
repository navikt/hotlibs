package no.nav.hjelpemidler.domain.person

import no.nav.hjelpemidler.text.isInteger
import no.nav.hjelpemidler.validation.Validator

/**
 * Navs personidentifikator med 11 siffer.
 */
class NPID(value: String) : PersonId(value) {
    init {
        require(erGyldig(value)) { "Ugyldig NPID" }
    }

    companion object : Validator<String> {
        private const val LENGTH = 11

        override fun erGyldig(value: String): Boolean = value.length == LENGTH && value.isInteger()
    }
}

/**
 * Konverter til [NPID].
 */
fun String.toNPID(): NPID = NPID(this)
