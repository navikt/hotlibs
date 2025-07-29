package no.nav.hjelpemidler.domain.person

import io.github.oshai.kotlinlogging.KotlinLogging
import no.nav.hjelpemidler.logging.teamError
import no.nav.hjelpemidler.text.isInteger

private val log = KotlinLogging.logger {}

/**
 * AktørId med 13 siffer.
 */
class AktørId(value: String) : PersonIdent(value) {
    init {
        if (!erGyldig(value)) {
            log.teamError { "Ugyldig aktørId: '$value'" }
            throw IllegalArgumentException("Ugyldig aktørId")
        }
    }

    companion object {
        fun erGyldig(value: String): Boolean = value.length == 13 && value.isInteger()
    }
}

/**
 * Konverter til [AktørId].
 */
fun String.toAktørId(): AktørId = AktørId(this)
