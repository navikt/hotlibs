package no.nav.hjelpemidler.domain.kodeverk

import com.fasterxml.jackson.annotation.JacksonAnnotationsInside
import com.fasterxml.jackson.annotation.JsonCreator
import no.nav.hjelpemidler.annotation.enumConstantAnnotation
import tools.jackson.core.JsonGenerator
import tools.jackson.databind.SerializationContext
import tools.jackson.databind.annotation.JsonSerialize
import tools.jackson.databind.ser.std.StdScalarSerializer

/**
 * Tilsvarer [com.fasterxml.jackson.annotation.JsonAlias], men tenkt brukt for kodeverk hvor vi typisk bruker
 * noe mindre kryptiske verdier.
 *
 * @see JsonSerializeEksterntKodeverk for å spesifisere at den eksterne representasjonen skal benyttes ved serialisering.
 */
@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
annotation class EksternKode(val value: String) {
    companion object {
        private val constantAnnotation = enumConstantAnnotation<EksternKode>()
        fun orNull(value: Enum<*>): String? = constantAnnotation.get(value)?.value
        fun of(value: Enum<*>): String = orNull(value) ?: value.name
    }
}

/**
 * Interface for eksterne kodeverk som i noen tilfeller bruker litt andre kodeverdier,
 */
interface EksterntKodeverk {
    /**
     * @see EksternKode
     */
    val eksternKode: String
        get() = if (this is Enum<*>) {
            EksternKode.of(this)
        } else {
            error("${javaClass.name} må implementere eksternKode")
        }

    companion object {
        @JvmStatic
        @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
        fun of(value: String): EksterntKodeverk? = if (value.isBlank()) null else Tekstkode<Nothing>(value)
    }
}

fun EksterntKodeverk?.isEqualTo(other: EksterntKodeverk?): Boolean = this?.eksternKode == other?.eksternKode

@JacksonAnnotationsInside
@JsonSerialize(using = EksterntKodeverkSerializer::class)
@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
annotation class JsonSerializeEksterntKodeverk

class EksterntKodeverkSerializer : StdScalarSerializer<EksterntKodeverk>(EksterntKodeverk::class.java) {
    override fun serialize(value: EksterntKodeverk, generator: JsonGenerator, context: SerializationContext) {
        val eksternKode = value.eksternKode
        if (eksternKode.isBlank()) {
            generator.writeNull()
        } else {
            generator.writeString(eksternKode)
        }
    }
}
