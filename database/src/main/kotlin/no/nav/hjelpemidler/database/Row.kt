package no.nav.hjelpemidler.database

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.JsonNode
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
import java.sql.Array
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

interface Row {
    fun any(columnIndex: Int): Any
    fun any(columnLabel: String): Any
    fun anyOrNull(columnIndex: Int): Any?
    fun anyOrNull(columnLabel: String): Any?
    fun asciiStream(columnIndex: Int): InputStream
    fun asciiStream(columnLabel: String): InputStream
    fun asciiStreamOrNull(columnIndex: Int): InputStream?
    fun asciiStreamOrNull(columnLabel: String): InputStream?
    fun bigDecimal(columnIndex: Int): BigDecimal
    fun bigDecimal(columnLabel: String): BigDecimal
    fun bigDecimalOrNull(columnIndex: Int): BigDecimal?
    fun bigDecimalOrNull(columnLabel: String): BigDecimal?
    fun binaryStream(columnIndex: Int): InputStream
    fun binaryStream(columnLabel: String): InputStream
    fun binaryStreamOrNull(columnIndex: Int): InputStream?
    fun binaryStreamOrNull(columnLabel: String): InputStream?
    fun blob(columnIndex: Int): Blob
    fun blob(columnLabel: String): Blob
    fun blobOrNull(columnIndex: Int): Blob?
    fun blobOrNull(columnLabel: String): Blob?
    fun boolean(columnIndex: Int): Boolean
    fun boolean(columnLabel: String): Boolean
    fun byte(columnIndex: Int): Byte
    fun byte(columnLabel: String): Byte
    fun byteOrNull(columnIndex: Int): Byte?
    fun byteOrNull(columnLabel: String): Byte?
    fun bytes(columnIndex: Int): ByteArray
    fun bytes(columnLabel: String): ByteArray
    fun bytesOrNull(columnIndex: Int): ByteArray?
    fun bytesOrNull(columnLabel: String): ByteArray?
    fun clob(columnIndex: Int): Clob
    fun clob(columnLabel: String): Clob
    fun clobOrNull(columnIndex: Int): Clob?
    fun clobOrNull(columnLabel: String): Clob?
    fun double(columnIndex: Int): Double
    fun double(columnLabel: String): Double
    fun doubleOrNull(columnIndex: Int): Double?
    fun doubleOrNull(columnLabel: String): Double?
    fun float(columnIndex: Int): Float
    fun float(columnLabel: String): Float
    fun floatOrNull(columnIndex: Int): Float?
    fun floatOrNull(columnLabel: String): Float?
    fun instant(columnIndex: Int): Instant
    fun instant(columnLabel: String): Instant
    fun instantOrNull(columnIndex: Int): Instant?
    fun instantOrNull(columnLabel: String): Instant?
    fun int(columnIndex: Int): Int
    fun int(columnLabel: String): Int
    fun intOrNull(columnIndex: Int): Int?
    fun intOrNull(columnLabel: String): Int?
    fun localDate(columnIndex: Int): LocalDate
    fun localDate(columnLabel: String): LocalDate
    fun localDateOrNull(columnIndex: Int): LocalDate?
    fun localDateOrNull(columnLabel: String): LocalDate?
    fun localDateTime(columnIndex: Int): LocalDateTime
    fun localDateTime(columnLabel: String): LocalDateTime
    fun localDateTimeOrNull(columnIndex: Int): LocalDateTime?
    fun localDateTimeOrNull(columnLabel: String): LocalDateTime?
    fun localTime(columnIndex: Int): LocalTime
    fun localTime(columnLabel: String): LocalTime
    fun localTimeOrNull(columnIndex: Int): LocalTime?
    fun localTimeOrNull(columnLabel: String): LocalTime?
    fun long(columnIndex: Int): Long
    fun long(columnLabel: String): Long
    fun longOrNull(columnIndex: Int): Long?
    fun longOrNull(columnLabel: String): Long?
    fun nCharacterStream(columnIndex: Int): Reader
    fun nCharacterStream(columnLabel: String): Reader
    fun nCharacterStreamOrNull(columnIndex: Int): Reader?
    fun nCharacterStreamOrNull(columnLabel: String): Reader?
    fun nClob(columnIndex: Int): NClob
    fun nClob(columnLabel: String): NClob
    fun nClobOrNull(columnIndex: Int): NClob?
    fun nClobOrNull(columnLabel: String): NClob?
    fun offsetDateTime(columnIndex: Int): OffsetDateTime
    fun offsetDateTime(columnLabel: String): OffsetDateTime
    fun offsetDateTimeOrNull(columnIndex: Int): OffsetDateTime?
    fun offsetDateTimeOrNull(columnLabel: String): OffsetDateTime?
    fun ref(columnIndex: Int): Ref
    fun ref(columnLabel: String): Ref
    fun refOrNull(columnIndex: Int): Ref?
    fun refOrNull(columnLabel: String): Ref?
    fun short(columnIndex: Int): Short
    fun short(columnLabel: String): Short
    fun shortOrNull(columnIndex: Int): Short?
    fun shortOrNull(columnLabel: String): Short?
    fun sqlArray(columnIndex: Int): Array
    fun sqlArray(columnLabel: String): Array
    fun sqlArrayOrNull(columnIndex: Int): Array?
    fun sqlArrayOrNull(columnLabel: String): Array?
    fun sqlDate(columnIndex: Int): Date
    fun sqlDate(columnIndex: Int, calendar: Calendar): Date
    fun sqlDate(columnLabel: String): Date
    fun sqlDate(columnLabel: String, calendar: Calendar): Date
    fun sqlDateOrNull(columnIndex: Int): Date?
    fun sqlDateOrNull(columnIndex: Int, calendar: Calendar): Date?
    fun sqlDateOrNull(columnLabel: String): Date?
    fun sqlDateOrNull(columnLabel: String, calendar: Calendar): Date?
    fun sqlTime(columnIndex: Int): Time
    fun sqlTime(columnIndex: Int, calendar: Calendar): Time
    fun sqlTime(columnLabel: String): Time
    fun sqlTime(columnLabel: String, calendar: Calendar): Time
    fun sqlTimeOrNull(columnIndex: Int): Time?
    fun sqlTimeOrNull(columnIndex: Int, calendar: Calendar): Time?
    fun sqlTimeOrNull(columnLabel: String): Time?
    fun sqlTimeOrNull(columnLabel: String, calendar: Calendar): Time?
    fun sqlTimestamp(columnIndex: Int): Timestamp
    fun sqlTimestamp(columnIndex: Int, calendar: Calendar): Timestamp
    fun sqlTimestamp(columnLabel: String): Timestamp
    fun sqlTimestamp(columnLabel: String, calendar: Calendar): Timestamp
    fun sqlTimestampOrNull(columnIndex: Int): Timestamp?
    fun sqlTimestampOrNull(columnIndex: Int, calendar: Calendar): Timestamp?
    fun sqlTimestampOrNull(columnLabel: String): Timestamp?
    fun sqlTimestampOrNull(columnLabel: String, calendar: Calendar): Timestamp?
    fun string(columnIndex: Int): String
    fun string(columnLabel: String): String
    fun stringOrNull(columnIndex: Int): String?
    fun stringOrNull(columnLabel: String): String?
    fun url(columnIndex: Int): URL
    fun url(columnLabel: String): URL
    fun urlOrNull(columnIndex: Int): URL?
    fun urlOrNull(columnLabel: String): URL?
    fun uuid(columnLabel: String): UUID
    fun uuidOrNull(columnLabel: String): UUID?
    fun zonedDateTime(columnIndex: Int): ZonedDateTime
    fun zonedDateTime(columnLabel: String): ZonedDateTime
    fun zonedDateTimeOrNull(columnIndex: Int): ZonedDateTime?
    fun zonedDateTimeOrNull(columnLabel: String): ZonedDateTime?

    fun <T> json(columnLabel: String, typeReference: TypeReference<T>): T =
        jsonMapper.readValue(string(columnLabel), typeReference)

    fun <T> jsonOrNull(columnLabel: String, typeReference: TypeReference<T>): T? =
        stringOrNull(columnLabel)?.let { jsonMapper.readValue(it, typeReference) }

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


    fun toMap(): Map<String, Any?>

    fun <K, V> Row.ifPresent(columnLabel: String, valueOrNull: Row.(String) -> K?, transform: Row.(K) -> V): V? {
        val value = valueOrNull(columnLabel) ?: return null
        return transform(value)
    }

    fun <T> ifLongPresent(columnLabel: String, transform: Row.(Long) -> T): T? =
        ifPresent(columnLabel, Row::longOrNull, transform)

    fun <T> ifStringPresent(columnLabel: String, transform: Row.(String) -> T): T? =
        ifPresent(columnLabel, Row::stringOrNull, transform)

    fun <T> ifUuidPresent(columnLabel: String, transform: Row.(UUID) -> T): T? =
        ifPresent(columnLabel, Row::uuidOrNull, transform)
}
