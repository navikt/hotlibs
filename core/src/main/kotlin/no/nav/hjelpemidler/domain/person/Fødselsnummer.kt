package no.nav.hjelpemidler.domain.person

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonIgnore
import no.bekk.bekkopen.person.Fodselsnummer
import no.bekk.bekkopen.person.FodselsnummerCalculator
import no.bekk.bekkopen.person.FodselsnummerValidator
import no.nav.hjelpemidler.logging.secureLog
import no.nav.hjelpemidler.time.toDate

/**
 * F-nummer/D-nummer med 11 siffer.
 *
 * Støtter også syntetiske verdier hvis [TILLAT_SYNTETISKE_FØDSELSNUMRE] settes til `true`.
 *
 * @see [Fodselsnummer]
 * @see [TILLAT_SYNTETISKE_FØDSELSNUMRE]
 */
@Serializable(with = Fødselsnummer.Serializer::class)
class Fødselsnummer @JsonIgnore private constructor(private val internal: Fodselsnummer) : PersonIdent(internal.value) {
    val kvinne: Boolean get() = internal.isFemale
    val mann: Boolean get() = internal.isMale
    val fødselsdato: Fødselsdato
        get() = Fødselsdato.of(
            internal.birthYear.toInt(),
            internal.month.toInt(),
            internal.dayInMonth.toInt(),
        )

    @JsonCreator
    constructor(value: String) : this(
        try {
            FodselsnummerValidator.getFodselsnummer(value)
        } catch (e: IllegalArgumentException) {
            secureLog.error(e) { "Ugyldig fødselsnummer: '$value'" }
            throw IllegalArgumentException("Ugyldig fødselsnummer")
        },
    )

    /**
     * Lag et tilfeldig fødselsnummer for [fødselsdato].
     */
    @JsonIgnore
    constructor(fødselsdato: Fødselsdato) : this(FodselsnummerCalculator.getFodselsnummerForDate(fødselsdato.toDate()))

    internal object Serializer : PersonIdent.Serializer<Fødselsnummer>() {
        override fun deserialize(value: String): Fødselsnummer = Fødselsnummer(value)
    }
}

/**
 * Konverter til [Fødselsnummer].
 */
fun String.toFødselsnummer(): Fødselsnummer = Fødselsnummer(this)

/**
 * Settes til `true` for å tillate syntetiske fødselsnumre.
 *
 * @see [FodselsnummerValidator.ALLOW_SYNTHETIC_NUMBERS]
 */
var TILLAT_SYNTETISKE_FØDSELSNUMRE: Boolean
    get() = FodselsnummerValidator.ALLOW_SYNTHETIC_NUMBERS
    set(value) {
        FodselsnummerValidator.ALLOW_SYNTHETIC_NUMBERS = value
    }
