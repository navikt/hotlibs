package no.nav.hjelpemidler.domain.kodeverk

import com.fasterxml.jackson.annotation.JacksonAnnotationsInside
import com.fasterxml.jackson.annotation.JsonCreator
import tools.jackson.core.JsonGenerator
import tools.jackson.databind.SerializationContext
import tools.jackson.databind.annotation.JsonSerialize
import tools.jackson.databind.ser.std.StdScalarSerializer

/**
 * Interface for eksterne kodeverk som i noen tilfeller bruker litt andre kodeverdier,
 */
interface EksterntKodeverk {
    val eksternKode: String

    companion object {
        @JvmStatic
        @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
        fun of(value: String): EksterntKodeverk? = if (value.isBlank()) {
            null
        } else {
            StringEksterntKodeverk(value)
        }
    }
}

fun EksterntKodeverk?.isEqualTo(other: EksterntKodeverk?): Boolean = this?.eksternKode == other?.eksternKode

@JvmInline
internal value class StringEksterntKodeverk(override val eksternKode: String) : EksterntKodeverk {
    init {
        require(eksternKode.isNotBlank()) { "'$eksternKode' er ikke en gyldig kode" }
    }

    override fun toString(): String = eksternKode
}

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

@JacksonAnnotationsInside
@JsonSerialize(using = EksterntKodeverkSerializer::class)
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FIELD)
annotation class JsonSerializeEksterntKodeverk
