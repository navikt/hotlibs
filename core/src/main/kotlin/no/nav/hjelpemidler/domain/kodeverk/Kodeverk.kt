package no.nav.hjelpemidler.domain.kodeverk

import com.fasterxml.jackson.databind.annotation.JsonDeserialize

/**
 * Felles interface for kodeverk.
 *
 * For ukjente kodeverdier benyttes [UkjentKode].
 *
 * @param E typen enum som inneholder "kjente" kodeverdier, dvs. kodeverdier som typisk skal benyttes i logikk.
 */
@JsonDeserialize(using = KodeverkDeserializer::class)
interface Kodeverk<E : Enum<E>> {
    val name: String
}

/**
 * Benyttes for å representere kodeverdier som systemet ikke kjenner til eller ikke bryr seg om, som mao. ikke brukes
 * i logikk.
 */
@JvmInline
value class UkjentKode<E : Enum<E>>(override val name: String) : Kodeverk<E> {
    override fun toString(): String = name
}
