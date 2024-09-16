package no.nav.hjelpemidler.database

import com.fasterxml.jackson.module.kotlin.jacksonTypeRef
import no.nav.hjelpemidler.collections.emptyEnumSet
import no.nav.hjelpemidler.collections.toEnumSet
import no.nav.hjelpemidler.domain.person.AktørId
import no.nav.hjelpemidler.domain.person.Fødselsnummer
import no.nav.hjelpemidler.domain.person.toAktørId
import no.nav.hjelpemidler.domain.person.toFødselsnummer
import java.util.UUID

typealias Row = kotliquery.Row

inline fun <reified T> Row.json(columnLabel: String): T =
    jsonMapper.readValue(string(columnLabel), jacksonTypeRef())

inline fun <reified T> Row.jsonOrNull(columnLabel: String): T? =
    stringOrNull(columnLabel)?.let {
        jsonMapper.readValue(it, jacksonTypeRef())
    }

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

fun <K, V> Row.ifPresent(columnLabel: String, valueOrNull: Row.(String) -> K?, transform: Row.(K) -> V): V? {
    val value = valueOrNull(columnLabel) ?: return null
    return transform(value)
}

fun <T> Row.ifLongPresent(columnLabel: String, transform: Row.(Long) -> T): T? =
    ifPresent(columnLabel, Row::longOrNull, transform)

fun <T> Row.ifStringPresent(columnLabel: String, transform: Row.(String) -> T): T? =
    ifPresent(columnLabel, Row::stringOrNull, transform)

fun <T> Row.ifUuidPresent(columnLabel: String, transform: Row.(UUID) -> T): T? =
    ifPresent(columnLabel, Row::uuidOrNull, transform)

fun Row.toMap(): Map<String, Any?> {
    val metaData = metaDataOrNull()
    return (1..metaData.columnCount).associate { columnIndex ->
        metaData.getColumnLabel(columnIndex) to anyOrNull(columnIndex)
    }
}

fun Row.aktørId(columnLabel: String): AktørId =
    string(columnLabel).toAktørId()

fun Row.aktørIdOrNull(columnLabel: String): AktørId? =
    stringOrNull(columnLabel)?.toAktørId()

fun Row.fødselsnummer(columnLabel: String): Fødselsnummer =
    string(columnLabel).toFødselsnummer()

fun Row.fødselsnummerOrNull(columnLabel: String): Fødselsnummer? =
    stringOrNull(columnLabel)?.toFødselsnummer()
