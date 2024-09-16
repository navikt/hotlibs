package no.nav.hjelpemidler.domain.person

import com.fasterxml.jackson.annotation.JsonCreator
import no.nav.hjelpemidler.logging.secureLog

/**
 * AktørId med 13 siffer.
 */
@Serializable(with = AktørId.Serializer::class)
class AktørId @JsonCreator constructor(value: String) : PersonIdent(value) {
    init {
        val valid = value matches regex
        if (!valid) {
            secureLog.error { "Ugyldig aktørId: '$value'" }
            throw IllegalArgumentException("Ugyldig aktørId")
        }
    }

    internal object Serializer : PersonIdent.Serializer<AktørId>() {
        override fun deserialize(value: String): AktørId = AktørId(value)
    }
}

/**
 * Konverter til [AktørId].
 */
fun String.toAktørId(): AktørId = AktørId(this)

private val regex: Regex = Regex("^[0-9]{13}$")
