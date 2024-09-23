package no.nav.hjelpemidler.domain.person

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonKey
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
@JvmInline
value class Fødselsnummer(override val value: String) : PersonIdent, CharSequence by value {
    init {
        if (!FodselsnummerValidator.isValid(value)) {
            secureLog.error { "Ugyldig fødselsnummer: '$value'" }
            throw IllegalArgumentException("Ugyldig fødselsnummer")
        }
    }

    private val internal: Fodselsnummer get() = FodselsnummerValidator.getFodselsnummer(value)

    val erKvinne: Boolean get() = internal.isFemale
    val erMann: Boolean get() = internal.isMale
    val fødselsdato: Fødselsdato
        get() = internal.let {
            Fødselsdato.of(
                it.birthYear.toInt(),
                it.month.toInt(),
                it.dayInMonth.toInt(),
            )
        }

    /**
     * Lag et tilfeldig fødselsnummer for [fødselsdato].
     */
    @JsonIgnore
    constructor(fødselsdato: Fødselsdato) : this(
        FodselsnummerCalculator
            .getFodselsnummerForDate(fødselsdato.toDate())
            .toString()
    )

    @JsonKey
    override fun toString(): String = value
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
