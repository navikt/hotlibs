package no.nav.hjelpemidler.database

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.module.kotlin.jacksonTypeRef
import no.nav.hjelpemidler.collections.emptyEnumSet
import no.nav.hjelpemidler.collections.toEnumSet
import no.nav.hjelpemidler.domain.enhet.Enhet
import no.nav.hjelpemidler.domain.geografi.Bydel
import no.nav.hjelpemidler.domain.geografi.Kommune
import no.nav.hjelpemidler.domain.person.AktørId
import no.nav.hjelpemidler.domain.person.Fødselsnummer
import no.nav.hjelpemidler.domain.person.Personnavn
import no.nav.hjelpemidler.domain.person.toAktørId
import no.nav.hjelpemidler.domain.person.toFødselsnummer
import no.nav.hjelpemidler.serialization.jackson.jsonMapper
import java.io.InputStream
import java.io.Reader
import java.math.BigDecimal
import java.net.URL
import java.sql.Blob
import java.sql.Clob
import java.sql.Date
import java.sql.NClob
import java.sql.Ref
import java.sql.Time
import java.sql.Timestamp
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.OffsetDateTime
import java.time.ZonedDateTime
import java.util.Calendar
import java.util.UUID

/**
 * Manuell delegering til [kotliquery.Row] siden [kotliquery.Row] ikke er et interface.
 */
class Row(private val wrapped: kotliquery.Row) : DatabaseRecord {
    fun any(columnIndex: Int): Any =
        wrapped.any(columnIndex)

    fun any(columnLabel: String): Any =
        wrapped.any(columnLabel)

    fun anyOrNull(columnIndex: Int): Any? =
        wrapped.anyOrNull(columnIndex)

    fun anyOrNull(columnLabel: String): Any? =
        wrapped.anyOrNull(columnLabel)

    inline fun <reified T> array(columnIndex: Int): Array<T> =
        arrayOrNull(columnIndex)!!

    inline fun <reified T> array(columnLabel: String): Array<T> =
        arrayOrNull(columnLabel)!!

    inline fun <reified T> arrayOrNull(columnIndex: Int): Array<T>? {
        val result = sqlArrayOrNull(columnIndex)?.array as Array<*>?
        return result?.map { it as T }?.toTypedArray()
    }

    inline fun <reified T> arrayOrNull(columnLabel: String): Array<T>? {
        val result = sqlArrayOrNull(columnLabel)?.array as Array<*>?
        return result?.map { it as T }?.toTypedArray()
    }

    fun asciiStream(columnIndex: Int): InputStream =
        wrapped.asciiStream(columnIndex)

    fun asciiStream(columnLabel: String): InputStream =
        wrapped.asciiStream(columnLabel)

    fun asciiStreamOrNull(columnIndex: Int): InputStream? =
        wrapped.asciiStreamOrNull(columnIndex)

    fun asciiStreamOrNull(columnLabel: String): InputStream? =
        wrapped.asciiStreamOrNull(columnLabel)

    fun bigDecimal(columnIndex: Int): BigDecimal =
        wrapped.bigDecimal(columnIndex)

    fun bigDecimal(columnLabel: String): BigDecimal =
        wrapped.bigDecimal(columnLabel)

    fun bigDecimalOrNull(columnIndex: Int): BigDecimal? =
        wrapped.bigDecimalOrNull(columnIndex)

    fun bigDecimalOrNull(columnLabel: String): BigDecimal? =
        wrapped.bigDecimalOrNull(columnLabel)

    fun binaryStream(columnIndex: Int): InputStream =
        wrapped.binaryStream(columnIndex)

    fun binaryStream(columnLabel: String): InputStream =
        wrapped.binaryStream(columnLabel)

    fun binaryStreamOrNull(columnIndex: Int): InputStream? =
        wrapped.binaryStreamOrNull(columnIndex)

    fun binaryStreamOrNull(columnLabel: String): InputStream? =
        wrapped.binaryStreamOrNull(columnLabel)

    fun blob(columnIndex: Int): Blob =
        wrapped.blob(columnIndex)

    fun blob(columnLabel: String): Blob =
        wrapped.blob(columnLabel)

    fun blobOrNull(columnIndex: Int): Blob? =
        wrapped.blobOrNull(columnIndex)

    fun blobOrNull(columnLabel: String): Blob? =
        wrapped.blobOrNull(columnLabel)

    fun boolean(columnIndex: Int): Boolean =
        wrapped.boolean(columnIndex)

    fun boolean(columnLabel: String): Boolean =
        wrapped.boolean(columnLabel)

    fun byte(columnIndex: Int): Byte =
        wrapped.byte(columnIndex)

    fun byte(columnLabel: String): Byte =
        wrapped.byte(columnLabel)

    fun byteOrNull(columnIndex: Int): Byte? =
        wrapped.byteOrNull(columnIndex)

    fun byteOrNull(columnLabel: String): Byte? =
        wrapped.byteOrNull(columnLabel)

    fun bytes(columnIndex: Int): ByteArray =
        wrapped.bytes(columnIndex)

    fun bytes(columnLabel: String): ByteArray =
        wrapped.bytes(columnLabel)

    fun bytesOrNull(columnIndex: Int): ByteArray? =
        wrapped.bytesOrNull(columnIndex)

    fun bytesOrNull(columnLabel: String): ByteArray? =
        wrapped.bytesOrNull(columnLabel)

    fun clob(columnIndex: Int): Clob =
        wrapped.clob(columnIndex)

    fun clob(columnLabel: String): Clob =
        wrapped.clob(columnLabel)

    fun clobOrNull(columnIndex: Int): Clob? =
        wrapped.clobOrNull(columnIndex)

    fun clobOrNull(columnLabel: String): Clob? =
        wrapped.clobOrNull(columnLabel)

    fun double(columnIndex: Int): Double =
        wrapped.double(columnIndex)

    fun double(columnLabel: String): Double =
        wrapped.double(columnLabel)

    fun doubleOrNull(columnIndex: Int): Double? =
        wrapped.doubleOrNull(columnIndex)

    fun doubleOrNull(columnLabel: String): Double? =
        wrapped.doubleOrNull(columnLabel)

    inline fun <reified E : Enum<E>> enum(columnLabel: String): E =
        enumValueOf(string((columnLabel)))

    inline fun <reified E : Enum<E>> enumOrNull(columnLabel: String): E? =
        stringOrNull(columnLabel)?.let<String, E>(::enumValueOf)

    inline fun <reified E : Enum<E>> enums(columnLabel: String): Set<E> =
        array<String>(columnLabel).toEnumSet()

    inline fun <reified E : Enum<E>> enumsOrNull(columnLabel: String): Set<E> {
        val strings = arrayOrNull<String>(columnLabel) ?: return emptyEnumSet()
        return strings.toEnumSet()
    }

    fun float(columnIndex: Int): Float =
        wrapped.float(columnIndex)

    fun float(columnLabel: String): Float =
        wrapped.float(columnLabel)

    fun floatOrNull(columnIndex: Int): Float? =
        wrapped.floatOrNull(columnIndex)

    fun floatOrNull(columnLabel: String): Float? =
        wrapped.floatOrNull(columnLabel)

    fun instant(columnIndex: Int): Instant =
        wrapped.instant(columnIndex)

    fun instant(columnLabel: String): Instant =
        wrapped.instant(columnLabel)

    fun instantOrNull(columnIndex: Int): Instant? =
        wrapped.instantOrNull(columnIndex)

    fun instantOrNull(columnLabel: String): Instant? =
        wrapped.instantOrNull(columnLabel)

    fun int(columnIndex: Int): Int =
        wrapped.int(columnIndex)

    fun int(columnLabel: String): Int =
        wrapped.int(columnLabel)

    fun intOrNull(columnIndex: Int): Int? =
        wrapped.intOrNull(columnIndex)

    fun intOrNull(columnLabel: String): Int? =
        wrapped.intOrNull(columnLabel)

    fun localDate(columnIndex: Int): LocalDate =
        wrapped.localDate(columnIndex)

    fun localDate(columnLabel: String): LocalDate =
        wrapped.localDate(columnLabel)

    fun localDateOrNull(columnIndex: Int): LocalDate? =
        wrapped.localDateOrNull(columnIndex)

    fun localDateOrNull(columnLabel: String): LocalDate? =
        wrapped.localDateOrNull(columnLabel)

    fun localDateTime(columnIndex: Int): LocalDateTime =
        wrapped.localDateTime(columnIndex)

    fun localDateTime(columnLabel: String): LocalDateTime =
        wrapped.localDateTime(columnLabel)

    fun localDateTimeOrNull(columnIndex: Int): LocalDateTime? =
        wrapped.localDateTimeOrNull(columnIndex)

    fun localDateTimeOrNull(columnLabel: String): LocalDateTime? =
        wrapped.localDateTimeOrNull(columnLabel)

    fun localTime(columnIndex: Int): LocalTime =
        wrapped.localTime(columnIndex)

    fun localTime(columnLabel: String): LocalTime =
        wrapped.localTime(columnLabel)

    fun localTimeOrNull(columnIndex: Int): LocalTime? =
        wrapped.localTimeOrNull(columnIndex)

    fun localTimeOrNull(columnLabel: String): LocalTime? =
        wrapped.localTimeOrNull(columnLabel)

    fun long(columnIndex: Int): Long =
        wrapped.long(columnIndex)

    fun long(columnLabel: String): Long =
        wrapped.long(columnLabel)

    fun longOrNull(columnIndex: Int): Long? =
        wrapped.longOrNull(columnIndex)

    fun longOrNull(columnLabel: String): Long? =
        wrapped.longOrNull(columnLabel)

    fun nCharacterStream(columnIndex: Int): Reader =
        wrapped.nCharacterStream(columnIndex)

    fun nCharacterStream(columnLabel: String): Reader =
        wrapped.nCharacterStream(columnLabel)

    fun nCharacterStreamOrNull(columnIndex: Int): Reader? =
        wrapped.nCharacterStreamOrNull(columnIndex)

    fun nCharacterStreamOrNull(columnLabel: String): Reader? =
        wrapped.nCharacterStreamOrNull(columnLabel)

    fun nClob(columnIndex: Int): NClob =
        wrapped.nClob(columnIndex)

    fun nClob(columnLabel: String): NClob =
        wrapped.nClob(columnLabel)

    fun nClobOrNull(columnIndex: Int): NClob? =
        wrapped.nClobOrNull(columnIndex)

    fun nClobOrNull(columnLabel: String): NClob? =
        wrapped.nClobOrNull(columnLabel)

    fun offsetDateTime(columnIndex: Int): OffsetDateTime =
        wrapped.offsetDateTime(columnIndex)

    fun offsetDateTime(columnLabel: String): OffsetDateTime =
        wrapped.offsetDateTime(columnLabel)

    fun offsetDateTimeOrNull(columnIndex: Int): OffsetDateTime? =
        wrapped.offsetDateTimeOrNull(columnIndex)

    fun offsetDateTimeOrNull(columnLabel: String): OffsetDateTime? =
        wrapped.offsetDateTimeOrNull(columnLabel)

    fun ref(columnIndex: Int): Ref =
        wrapped.ref(columnIndex)

    fun ref(columnLabel: String): Ref =
        wrapped.ref(columnLabel)

    fun refOrNull(columnIndex: Int): Ref? =
        wrapped.refOrNull(columnIndex)

    fun refOrNull(columnLabel: String): Ref? =
        wrapped.refOrNull(columnLabel)

    fun short(columnIndex: Int): Short =
        wrapped.short(columnIndex)

    fun short(columnLabel: String): Short =
        wrapped.short(columnLabel)

    fun shortOrNull(columnIndex: Int): Short? =
        wrapped.shortOrNull(columnIndex)

    fun shortOrNull(columnLabel: String): Short? =
        wrapped.shortOrNull(columnLabel)

    fun sqlArray(columnIndex: Int): java.sql.Array =
        wrapped.sqlArray(columnIndex)

    fun sqlArray(columnLabel: String): java.sql.Array =
        wrapped.sqlArray(columnLabel)

    fun sqlArrayOrNull(columnIndex: Int): java.sql.Array? =
        wrapped.sqlArrayOrNull(columnIndex)

    fun sqlArrayOrNull(columnLabel: String): java.sql.Array? =
        wrapped.sqlArrayOrNull(columnLabel)

    fun sqlDate(columnIndex: Int): Date =
        wrapped.sqlDate(columnIndex)

    fun sqlDate(columnIndex: Int, calendar: Calendar): Date =
        wrapped.sqlDate(columnIndex, calendar)

    fun sqlDate(columnLabel: String): Date =
        wrapped.sqlDate(columnLabel)

    fun sqlDate(columnLabel: String, calendar: Calendar): Date =
        wrapped.sqlDate(columnLabel, calendar)

    fun sqlDateOrNull(columnIndex: Int): Date? =
        wrapped.sqlDateOrNull(columnIndex)

    fun sqlDateOrNull(columnIndex: Int, calendar: Calendar): Date? =
        wrapped.sqlDateOrNull(columnIndex, calendar)

    fun sqlDateOrNull(columnLabel: String): Date? =
        wrapped.sqlDateOrNull(columnLabel)

    fun sqlDateOrNull(columnLabel: String, calendar: Calendar): Date? =
        wrapped.sqlDateOrNull(columnLabel, calendar)

    fun sqlTime(columnIndex: Int): Time =
        wrapped.sqlTime(columnIndex)

    fun sqlTime(columnIndex: Int, calendar: Calendar): Time =
        wrapped.sqlTime(columnIndex, calendar)

    fun sqlTime(columnLabel: String): Time =
        wrapped.sqlTime(columnLabel)

    fun sqlTime(columnLabel: String, calendar: Calendar): Time =
        wrapped.sqlTime(columnLabel, calendar)

    fun sqlTimeOrNull(columnIndex: Int): Time? =
        wrapped.sqlTimeOrNull(columnIndex)

    fun sqlTimeOrNull(columnIndex: Int, calendar: Calendar): Time? =
        wrapped.sqlTimeOrNull(columnIndex, calendar)

    fun sqlTimeOrNull(columnLabel: String): Time? =
        wrapped.sqlTimeOrNull(columnLabel)

    fun sqlTimeOrNull(columnLabel: String, calendar: Calendar): Time? =
        wrapped.sqlTimeOrNull(columnLabel, calendar)

    fun sqlTimestamp(columnIndex: Int): Timestamp =
        wrapped.sqlTimestamp(columnIndex)

    fun sqlTimestamp(columnIndex: Int, calendar: Calendar): Timestamp =
        wrapped.sqlTimestamp(columnIndex, calendar)

    fun sqlTimestamp(columnLabel: String): Timestamp =
        wrapped.sqlTimestamp(columnLabel)

    fun sqlTimestamp(columnLabel: String, calendar: Calendar): Timestamp =
        wrapped.sqlTimestamp(columnLabel, calendar)

    fun sqlTimestampOrNull(columnIndex: Int): Timestamp? =
        wrapped.sqlTimestampOrNull(columnIndex)

    fun sqlTimestampOrNull(columnIndex: Int, calendar: Calendar): Timestamp? =
        wrapped.sqlTimestampOrNull(columnIndex, calendar)

    fun sqlTimestampOrNull(columnLabel: String): Timestamp? =
        wrapped.sqlTimestampOrNull(columnLabel)

    fun sqlTimestampOrNull(columnLabel: String, calendar: Calendar): Timestamp? =
        wrapped.sqlTimestampOrNull(columnLabel, calendar)

    fun string(columnIndex: Int): String =
        wrapped.string(columnIndex)

    fun string(columnLabel: String): String =
        wrapped.string(columnLabel)

    fun stringOrNull(columnIndex: Int): String? =
        wrapped.stringOrNull(columnIndex)

    fun stringOrNull(columnLabel: String): String? =
        wrapped.stringOrNull(columnLabel)

    fun url(columnIndex: Int): URL =
        wrapped.url(columnIndex)

    fun url(columnLabel: String): URL =
        wrapped.url(columnLabel)

    fun urlOrNull(columnIndex: Int): URL? =
        wrapped.urlOrNull(columnIndex)

    fun urlOrNull(columnLabel: String): URL? =
        wrapped.urlOrNull(columnLabel)

    fun uuid(columnLabel: String): UUID =
        wrapped.uuid(columnLabel)

    fun uuidOrNull(columnLabel: String): UUID? =
        wrapped.uuidOrNull(columnLabel)

    fun zonedDateTime(columnIndex: Int): ZonedDateTime =
        wrapped.zonedDateTime(columnIndex)

    fun zonedDateTime(columnLabel: String): ZonedDateTime =
        wrapped.zonedDateTime(columnLabel)

    fun zonedDateTimeOrNull(columnIndex: Int): ZonedDateTime? =
        wrapped.zonedDateTimeOrNull(columnIndex)

    fun zonedDateTimeOrNull(columnLabel: String): ZonedDateTime? =
        wrapped.zonedDateTimeOrNull(columnLabel)

    fun <T : Any> json(columnLabel: String, typeReference: TypeReference<T>): T =
        jsonMapper.readValue(string(columnLabel), typeReference)

    inline fun <reified T : Any> json(columnLabel: String): T =
        json(columnLabel, jacksonTypeRef<T>())

    fun <T> jsonOrNull(columnLabel: String, typeReference: TypeReference<T>): T? =
        stringOrNull(columnLabel)?.let { jsonMapper.readValue(it, typeReference) }

    inline fun <reified T> jsonOrNull(columnLabel: String): T? =
        jsonOrNull(columnLabel, jacksonTypeRef<T>())

    fun tree(columnLabel: String): JsonNode =
        jsonMapper.readTree(string(columnLabel))

    fun treeOrNull(columnLabel: String): JsonNode? =
        stringOrNull(columnLabel)?.let<String, JsonNode?>(jsonMapper::readTree)

    fun aktørId(columnLabel: String): AktørId =
        string(columnLabel).toAktørId()

    fun aktørIdOrNull(columnLabel: String): AktørId? =
        stringOrNull(columnLabel)?.toAktørId()

    fun fødselsnummer(columnLabel: String): Fødselsnummer =
        string(columnLabel).toFødselsnummer()

    fun fødselsnummerOrNull(columnLabel: String): Fødselsnummer? =
        stringOrNull(columnLabel)?.toFødselsnummer()

    fun toPersonnavn(prefix: String? = null): Personnavn =
        Personnavn(
            fornavn = string(columnLabelOf("fornavn", prefix)),
            mellomnavn = stringOrNull(columnLabelOf("mellomnavn", prefix)),
            etternavn = string(columnLabelOf("etternavn", prefix)),
        )

    fun toEnhet(prefix: String? = null): Enhet =
        Enhet(
            nummer = string(columnLabelOf("enhetsnummer", prefix)),
            navn = string(columnLabelOf("enhetsnavn", prefix)),
        )

    fun toKommuneOrNull(prefix: String? = null): Kommune? =
        ifStringPresent(columnLabelOf("kommunenummer", prefix)) { nummer ->
            Kommune(nummer = nummer, navn = string(columnLabelOf("kommunenavn", prefix)))
        }

    fun toBydelOrNull(prefix: String? = null): Bydel? =
        ifStringPresent(columnLabelOf("bydelsnummer", prefix)) { nummer ->
            Bydel(nummer = nummer, navn = string(columnLabelOf("bydelsnavn", prefix)))
        }

    fun <K, V> ifPresent(columnLabel: String, valueOrNull: Row.(String) -> K?, transform: Row.(K) -> V): V? {
        val value = valueOrNull(columnLabel) ?: return null
        return transform(value)
    }

    fun <T> ifLongPresent(columnLabel: String, transform: Row.(Long) -> T): T? =
        ifPresent(columnLabel, Row::longOrNull, transform)

    fun <T> ifStringPresent(columnLabel: String, transform: Row.(String) -> T): T? =
        ifPresent(columnLabel, Row::stringOrNull, transform)

    fun <T> ifUuidPresent(columnLabel: String, transform: Row.(UUID) -> T): T? =
        ifPresent(columnLabel, Row::uuidOrNull, transform)

    override fun toMap(): Map<String, Any?> {
        val metaData = wrapped.metaDataOrNull()
        return (1..metaData.columnCount).associate { columnIndex ->
            metaData.getColumnLabel(columnIndex) to anyOrNull(columnIndex)
        }
    }
}
