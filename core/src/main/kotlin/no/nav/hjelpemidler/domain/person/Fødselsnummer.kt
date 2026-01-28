package no.nav.hjelpemidler.domain.person

import com.fasterxml.jackson.annotation.JsonIgnore
import no.bekk.bekkopen.person.Fodselsnummer
import no.bekk.bekkopen.person.FodselsnummerCalculator
import no.bekk.bekkopen.person.FodselsnummerValidator
import no.nav.hjelpemidler.time.toDate
import no.nav.hjelpemidler.validation.Validator

/**
 * F-nummer/D-nummer med 11 siffer.
 *
 * Støtter også syntetiske verdier hvis [TILLAT_SYNTETISKE_FØDSELSNUMRE] settes til `true`.
 *
 * @see [Fodselsnummer]
 * @see [TILLAT_SYNTETISKE_FØDSELSNUMRE]
 */
class Fødselsnummer(value: String) : PersonIdent(value) {
    init {
        require(erGyldig(value)) { "Ugyldig fødselsnummer" }
    }

    private val internal: Fodselsnummer get() = FodselsnummerValidator.getFodselsnummer(value)

    @Deprecated("Kjønn kan ikke sikkert utledes fra fødselsnummer, hent informasjonen fra PDL.")
    val erKvinne: Boolean get() = internal.isFemale

    @Deprecated("Kjønn kan ikke sikkert utledes fra fødselsnummer, hent informasjonen fra PDL.")
    val erMann: Boolean get() = internal.isMale

    @Deprecated("Fødselsdato kan ikke sikkert utledes fra fødselsnummer, hent informasjonen fra PDL.")
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

    companion object : Validator<String> {
        override fun erGyldig(value: String): Boolean = FodselsnummerValidator.isValid(value)
    }
}

/**
 * Konverter til [Fødselsnummer].
 */
fun String.toFødselsnummer(): Fødselsnummer = Fødselsnummer(this)

/**
 * Konverter til [Fødselsnummer].
 */
@JvmName("toFødselsnummerOrNull")
fun String?.toFødselsnummer(): Fødselsnummer? = this?.toFødselsnummer()

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
