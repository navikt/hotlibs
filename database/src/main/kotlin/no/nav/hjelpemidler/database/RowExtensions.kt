package no.nav.hjelpemidler.database

import com.fasterxml.jackson.module.kotlin.jacksonTypeRef
import no.nav.hjelpemidler.collections.emptyEnumSet
import no.nav.hjelpemidler.collections.toEnumSet
import no.nav.hjelpemidler.domain.geografi.Bydel
import no.nav.hjelpemidler.domain.geografi.Enhet
import no.nav.hjelpemidler.domain.geografi.Kommune
import no.nav.hjelpemidler.domain.person.Personnavn

inline fun <reified T> Row.array(columnIndex: Int): Array<T> =
    arrayOrNull(columnIndex)!!

inline fun <reified T> Row.array(columnLabel: String): Array<T> =
    arrayOrNull(columnLabel)!!

inline fun <reified T> Row.arrayOrNull(columnIndex: Int): Array<T>? {
    val result = sqlArrayOrNull(columnIndex)?.array as Array<*>?
    return result?.map { it as T }?.toTypedArray()
}

inline fun <reified T> Row.arrayOrNull(columnLabel: String): Array<T>? {
    val result = sqlArrayOrNull(columnLabel)?.array as Array<*>?
    return result?.map { it as T }?.toTypedArray()
}

inline fun <reified T> Row.json(columnLabel: String): T =
    json(columnLabel, jacksonTypeRef<T>())

inline fun <reified T> Row.jsonOrNull(columnLabel: String): T? =
    jsonOrNull(columnLabel, jacksonTypeRef<T>())

inline fun <reified E : Enum<E>> Row.enum(columnLabel: String): E =
    enumValueOf(string((columnLabel)))

inline fun <reified E : Enum<E>> Row.enumOrNull(columnLabel: String): E? =
    stringOrNull(columnLabel)?.let<String, E>(::enumValueOf)

inline fun <reified E : Enum<E>> Row.enums(columnLabel: String): Set<E> =
    array<String>(columnLabel).toEnumSet()

inline fun <reified E : Enum<E>> Row.enumsOrNull(columnLabel: String): Set<E> {
    val strings = arrayOrNull<String>(columnLabel) ?: return emptyEnumSet()
    return strings.toEnumSet()
}

// Personnavn

fun Personnavn?.toQueryParameters(prefix: String? = null): QueryParameters = mapOf(
    "fornavn" to this?.fornavn,
    "mellomnavn" to this?.mellomnavn,
    "etternavn" to this?.etternavn,
).transform(prefix)

// Enhet

fun Enhet.toQueryParameters(): QueryParameters =
    mapOf("enhetsnummer" to nummer, "enhetsnavn" to navn)

// Kommune

fun Kommune.toQueryParameters(prefix: String? = null): QueryParameters = mapOf(
    "kommunenummer" to nummer,
    "kommunenavn" to navn,
).transform(prefix = prefix)

// Bydel

fun Bydel.toQueryParameters(prefix: String? = null): QueryParameters = mapOf(
    "bydelsnummer" to nummer,
    "bydelsnavn" to navn,
).transform(prefix = prefix)
