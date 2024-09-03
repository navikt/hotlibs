package no.nav.hjelpemidler.domain.person

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonIgnore
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import no.bekk.bekkopen.person.Fodselsnummer
import no.bekk.bekkopen.person.FodselsnummerCalculator
import no.bekk.bekkopen.person.FodselsnummerValidator
import no.nav.hjelpemidler.logging.secureLog
import no.nav.hjelpemidler.time.toDate

/**
 * FNR/DNR med 11 siffer. Støtter også syntetiske verdier.
 *
 * @see [Fodselsnummer]
 */
@Serializable(with = Fødselsnummer.Serializer::class)
class Fødselsnummer @JsonIgnore private constructor(
    private val internal: Fodselsnummer,
) : PersonIdent, CharSequence by internal.value {
    override val value: String get() = internal.value

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

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as Fødselsnummer
        return internal == other.internal
    }

    override fun hashCode(): Int = internal.hashCode()

    override fun toString(): String = internal.toString()

    internal class Serializer : KSerializer<Fødselsnummer> {
        override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor(
            "no.nav.hjelpemidler.domain.person.Fødselsnummer",
            PrimitiveKind.STRING,
        )

        override fun serialize(encoder: Encoder, value: Fødselsnummer) = encoder.encodeString(value.toString())
        override fun deserialize(decoder: Decoder): Fødselsnummer = Fødselsnummer(decoder.decodeString())
    }
}

fun String.toFødselsnummer(): Fødselsnummer = Fødselsnummer(this)

var TILLAT_SYNTETISKE_FØDSELSNUMRE: Boolean
    get() = FodselsnummerValidator.ALLOW_SYNTHETIC_NUMBERS
    set(value) {
        FodselsnummerValidator.ALLOW_SYNTHETIC_NUMBERS = value
    }
