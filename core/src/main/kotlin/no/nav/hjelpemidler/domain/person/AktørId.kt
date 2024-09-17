package no.nav.hjelpemidler.domain.person

import com.fasterxml.jackson.annotation.JsonCreator
import kotlinx.serialization.Serializable
import no.nav.hjelpemidler.logging.secureLog

/**
 * AktørId med 13 siffer.
 */
@Serializable(with = AktørIdSerializer::class)
class AktørId @JsonCreator constructor(value: String) : PersonIdent(value) {
    init {
        val valid = value matches regex
        if (!valid) {
            secureLog.error { "Ugyldig aktørId: '$value'" }
            throw IllegalArgumentException("Ugyldig aktørId")
        }
    }
}

/**
 * Konverter til [AktørId].
 */
fun String.toAktørId(): AktørId = AktørId(this)

private val regex: Regex = Regex("^[0-9]{13}$")
