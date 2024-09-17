package no.nav.hjelpemidler.domain.id

import com.fasterxml.jackson.annotation.JsonValue

/**
 * Klasse som danner grunnlag for implementasjon av sterke typer for ulike identifikatorer.
 * Dette som et alternativ til å bruke primitive typer direkte.
 *
 * Bruk av [JsonValue] sikrer at kun [value] havner i JSON ved serialisering.
 * Konstruktøren(e) i konkrete implementasjoner brukes ved deserialisering.
 * Bruk evt. [com.fasterxml.jackson.annotation.JsonCreator] for å markere hvilke(n) konstruktør(er) som
 * skal benyttes.
 *
 * Det er også lagt opp til bruk av ktor-resources med [Id.Serializer]. Implementasjoner av denne sikrer
 * riktig (de)serialisering ved bruk av kotlinx-serialization som brukes under panseret i ktor-resources.
 *
 * NB! Vi serialiserer alltid verdien til [String] i JSON.
 */
abstract class Id<out T : Any>(val value: T) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as Id<*>
        return value == other.value
    }

    override fun hashCode(): Int = value.hashCode()

    @JsonValue
    override fun toString(): String = value.toString()
}
