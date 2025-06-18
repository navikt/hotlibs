package no.nav.hjelpemidler.database

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

internal class ResultSetRow(private val wrapped: kotliquery.Row) : Row {
    override fun any(columnIndex: Int): Any =
        wrapped.any(columnIndex)

    override fun any(columnLabel: String): Any =
        wrapped.any(columnLabel)

    override fun anyOrNull(columnIndex: Int): Any? =
        wrapped.anyOrNull(columnIndex)

    override fun anyOrNull(columnLabel: String): Any? =
        wrapped.anyOrNull(columnLabel)

    override fun asciiStream(columnIndex: Int): InputStream =
        wrapped.asciiStream(columnIndex)

    override fun asciiStream(columnLabel: String): InputStream =
        wrapped.asciiStream(columnLabel)

    override fun asciiStreamOrNull(columnIndex: Int): InputStream? =
        wrapped.asciiStreamOrNull(columnIndex)

    override fun asciiStreamOrNull(columnLabel: String): InputStream? =
        wrapped.asciiStreamOrNull(columnLabel)

    override fun bigDecimal(columnIndex: Int): BigDecimal =
        wrapped.bigDecimal(columnIndex)

    override fun bigDecimal(columnLabel: String): BigDecimal =
        wrapped.bigDecimal(columnLabel)

    override fun bigDecimalOrNull(columnIndex: Int): BigDecimal? =
        wrapped.bigDecimalOrNull(columnIndex)

    override fun bigDecimalOrNull(columnLabel: String): BigDecimal? =
        wrapped.bigDecimalOrNull(columnLabel)

    override fun binaryStream(columnIndex: Int): InputStream =
        wrapped.binaryStream(columnIndex)

    override fun binaryStream(columnLabel: String): InputStream =
        wrapped.binaryStream(columnLabel)

    override fun binaryStreamOrNull(columnIndex: Int): InputStream? =
        wrapped.binaryStreamOrNull(columnIndex)

    override fun binaryStreamOrNull(columnLabel: String): InputStream? =
        wrapped.binaryStreamOrNull(columnLabel)

    override fun blob(columnIndex: Int): Blob =
        wrapped.blob(columnIndex)

    override fun blob(columnLabel: String): Blob =
        wrapped.blob(columnLabel)

    override fun blobOrNull(columnIndex: Int): Blob? =
        wrapped.blobOrNull(columnIndex)

    override fun blobOrNull(columnLabel: String): Blob? =
        wrapped.blobOrNull(columnLabel)

    override fun boolean(columnIndex: Int): Boolean =
        wrapped.boolean(columnIndex)

    override fun boolean(columnLabel: String): Boolean =
        wrapped.boolean(columnLabel)

    override fun byte(columnIndex: Int): Byte =
        wrapped.byte(columnIndex)

    override fun byte(columnLabel: String): Byte =
        wrapped.byte(columnLabel)

    override fun byteOrNull(columnIndex: Int): Byte? =
        wrapped.byteOrNull(columnIndex)

    override fun byteOrNull(columnLabel: String): Byte? =
        wrapped.byteOrNull(columnLabel)

    override fun bytes(columnIndex: Int): ByteArray =
        wrapped.bytes(columnIndex)

    override fun bytes(columnLabel: String): ByteArray =
        wrapped.bytes(columnLabel)

    override fun bytesOrNull(columnIndex: Int): ByteArray? =
        wrapped.bytesOrNull(columnIndex)

    override fun bytesOrNull(columnLabel: String): ByteArray? =
        wrapped.bytesOrNull(columnLabel)

    override fun clob(columnIndex: Int): Clob =
        wrapped.clob(columnIndex)

    override fun clob(columnLabel: String): Clob =
        wrapped.clob(columnLabel)

    override fun clobOrNull(columnIndex: Int): Clob? =
        wrapped.clobOrNull(columnIndex)

    override fun clobOrNull(columnLabel: String): Clob? =
        wrapped.clobOrNull(columnLabel)

    override fun double(columnIndex: Int): Double =
        wrapped.double(columnIndex)

    override fun double(columnLabel: String): Double =
        wrapped.double(columnLabel)

    override fun doubleOrNull(columnIndex: Int): Double? =
        wrapped.doubleOrNull(columnIndex)

    override fun doubleOrNull(columnLabel: String): Double? =
        wrapped.doubleOrNull(columnLabel)

    override fun float(columnIndex: Int): Float =
        wrapped.float(columnIndex)

    override fun float(columnLabel: String): Float =
        wrapped.float(columnLabel)

    override fun floatOrNull(columnIndex: Int): Float? =
        wrapped.floatOrNull(columnIndex)

    override fun floatOrNull(columnLabel: String): Float? =
        wrapped.floatOrNull(columnLabel)

    override fun instant(columnIndex: Int): Instant =
        wrapped.instant(columnIndex)

    override fun instant(columnLabel: String): Instant =
        wrapped.instant(columnLabel)

    override fun instantOrNull(columnIndex: Int): Instant? =
        wrapped.instantOrNull(columnIndex)

    override fun instantOrNull(columnLabel: String): Instant? =
        wrapped.instantOrNull(columnLabel)

    override fun int(columnIndex: Int): Int =
        wrapped.int(columnIndex)

    override fun int(columnLabel: String): Int =
        wrapped.int(columnLabel)

    override fun intOrNull(columnIndex: Int): Int? =
        wrapped.intOrNull(columnIndex)

    override fun intOrNull(columnLabel: String): Int? =
        wrapped.intOrNull(columnLabel)

    override fun localDate(columnIndex: Int): LocalDate =
        wrapped.localDate(columnIndex)

    override fun localDate(columnLabel: String): LocalDate =
        wrapped.localDate(columnLabel)

    override fun localDateOrNull(columnIndex: Int): LocalDate? =
        wrapped.localDateOrNull(columnIndex)

    override fun localDateOrNull(columnLabel: String): LocalDate? =
        wrapped.localDateOrNull(columnLabel)

    override fun localDateTime(columnIndex: Int): LocalDateTime =
        wrapped.localDateTime(columnIndex)

    override fun localDateTime(columnLabel: String): LocalDateTime =
        wrapped.localDateTime(columnLabel)

    override fun localDateTimeOrNull(columnIndex: Int): LocalDateTime? =
        wrapped.localDateTimeOrNull(columnIndex)

    override fun localDateTimeOrNull(columnLabel: String): LocalDateTime? =
        wrapped.localDateTimeOrNull(columnLabel)

    override fun localTime(columnIndex: Int): LocalTime =
        wrapped.localTime(columnIndex)

    override fun localTime(columnLabel: String): LocalTime =
        wrapped.localTime(columnLabel)

    override fun localTimeOrNull(columnIndex: Int): LocalTime? =
        wrapped.localTimeOrNull(columnIndex)

    override fun localTimeOrNull(columnLabel: String): LocalTime? =
        wrapped.localTimeOrNull(columnLabel)

    override fun long(columnIndex: Int): Long =
        wrapped.long(columnIndex)

    override fun long(columnLabel: String): Long =
        wrapped.long(columnLabel)

    override fun longOrNull(columnIndex: Int): Long? =
        wrapped.longOrNull(columnIndex)

    override fun longOrNull(columnLabel: String): Long? =
        wrapped.longOrNull(columnLabel)

    override fun nCharacterStream(columnIndex: Int): Reader =
        wrapped.nCharacterStream(columnIndex)

    override fun nCharacterStream(columnLabel: String): Reader =
        wrapped.nCharacterStream(columnLabel)

    override fun nCharacterStreamOrNull(columnIndex: Int): Reader? =
        wrapped.nCharacterStreamOrNull(columnIndex)

    override fun nCharacterStreamOrNull(columnLabel: String): Reader? =
        wrapped.nCharacterStreamOrNull(columnLabel)

    override fun nClob(columnIndex: Int): NClob =
        wrapped.nClob(columnIndex)

    override fun nClob(columnLabel: String): NClob =
        wrapped.nClob(columnLabel)

    override fun nClobOrNull(columnIndex: Int): NClob? =
        wrapped.nClobOrNull(columnIndex)

    override fun nClobOrNull(columnLabel: String): NClob? =
        wrapped.nClobOrNull(columnLabel)

    override fun offsetDateTime(columnIndex: Int): OffsetDateTime =
        wrapped.offsetDateTime(columnIndex)

    override fun offsetDateTime(columnLabel: String): OffsetDateTime =
        wrapped.offsetDateTime(columnLabel)

    override fun offsetDateTimeOrNull(columnIndex: Int): OffsetDateTime? =
        wrapped.offsetDateTimeOrNull(columnIndex)

    override fun offsetDateTimeOrNull(columnLabel: String): OffsetDateTime? =
        wrapped.offsetDateTimeOrNull(columnLabel)

    override fun ref(columnIndex: Int): Ref =
        wrapped.ref(columnIndex)

    override fun ref(columnLabel: String): Ref =
        wrapped.ref(columnLabel)

    override fun refOrNull(columnIndex: Int): Ref? =
        wrapped.refOrNull(columnIndex)

    override fun refOrNull(columnLabel: String): Ref? =
        wrapped.refOrNull(columnLabel)

    override fun short(columnIndex: Int): Short =
        wrapped.short(columnIndex)

    override fun short(columnLabel: String): Short =
        wrapped.short(columnLabel)

    override fun shortOrNull(columnIndex: Int): Short? =
        wrapped.shortOrNull(columnIndex)

    override fun shortOrNull(columnLabel: String): Short? =
        wrapped.shortOrNull(columnLabel)

    override fun sqlArray(columnIndex: Int): Array =
        wrapped.sqlArray(columnIndex)

    override fun sqlArray(columnLabel: String): Array =
        wrapped.sqlArray(columnLabel)

    override fun sqlArrayOrNull(columnIndex: Int): Array? =
        wrapped.sqlArrayOrNull(columnIndex)

    override fun sqlArrayOrNull(columnLabel: String): Array? =
        wrapped.sqlArrayOrNull(columnLabel)

    override fun sqlDate(columnIndex: Int): Date =
        wrapped.sqlDate(columnIndex)

    override fun sqlDate(columnIndex: Int, calendar: Calendar): Date =
        wrapped.sqlDate(columnIndex, calendar)

    override fun sqlDate(columnLabel: String): Date =
        wrapped.sqlDate(columnLabel)

    override fun sqlDate(columnLabel: String, calendar: Calendar): Date =
        wrapped.sqlDate(columnLabel, calendar)

    override fun sqlDateOrNull(columnIndex: Int): Date? =
        wrapped.sqlDateOrNull(columnIndex)

    override fun sqlDateOrNull(columnIndex: Int, calendar: Calendar): Date? =
        wrapped.sqlDateOrNull(columnIndex, calendar)

    override fun sqlDateOrNull(columnLabel: String): Date? =
        wrapped.sqlDateOrNull(columnLabel)

    override fun sqlDateOrNull(columnLabel: String, calendar: Calendar): Date? =
        wrapped.sqlDateOrNull(columnLabel, calendar)

    override fun sqlTime(columnIndex: Int): Time =
        wrapped.sqlTime(columnIndex)

    override fun sqlTime(columnIndex: Int, calendar: Calendar): Time =
        wrapped.sqlTime(columnIndex, calendar)

    override fun sqlTime(columnLabel: String): Time =
        wrapped.sqlTime(columnLabel)

    override fun sqlTime(columnLabel: String, calendar: Calendar): Time =
        wrapped.sqlTime(columnLabel, calendar)

    override fun sqlTimeOrNull(columnIndex: Int): Time? =
        wrapped.sqlTimeOrNull(columnIndex)

    override fun sqlTimeOrNull(columnIndex: Int, calendar: Calendar): Time? =
        wrapped.sqlTimeOrNull(columnIndex, calendar)

    override fun sqlTimeOrNull(columnLabel: String): Time? =
        wrapped.sqlTimeOrNull(columnLabel)

    override fun sqlTimeOrNull(columnLabel: String, calendar: Calendar): Time? =
        wrapped.sqlTimeOrNull(columnLabel, calendar)

    override fun sqlTimestamp(columnIndex: Int): Timestamp =
        wrapped.sqlTimestamp(columnIndex)

    override fun sqlTimestamp(columnIndex: Int, calendar: Calendar): Timestamp =
        wrapped.sqlTimestamp(columnIndex, calendar)

    override fun sqlTimestamp(columnLabel: String): Timestamp =
        wrapped.sqlTimestamp(columnLabel)

    override fun sqlTimestamp(columnLabel: String, calendar: Calendar): Timestamp =
        wrapped.sqlTimestamp(columnLabel, calendar)

    override fun sqlTimestampOrNull(columnIndex: Int): Timestamp? =
        wrapped.sqlTimestampOrNull(columnIndex)

    override fun sqlTimestampOrNull(columnIndex: Int, calendar: Calendar): Timestamp? =
        wrapped.sqlTimestampOrNull(columnIndex, calendar)

    override fun sqlTimestampOrNull(columnLabel: String): Timestamp? =
        wrapped.sqlTimestampOrNull(columnLabel)

    override fun sqlTimestampOrNull(columnLabel: String, calendar: Calendar): Timestamp? =
        wrapped.sqlTimestampOrNull(columnLabel, calendar)

    override fun string(columnIndex: Int): String =
        wrapped.string(columnIndex)

    override fun string(columnLabel: String): String =
        wrapped.string(columnLabel)

    override fun stringOrNull(columnIndex: Int): String? =
        wrapped.stringOrNull(columnIndex)

    override fun stringOrNull(columnLabel: String): String? =
        wrapped.stringOrNull(columnLabel)

    override fun url(columnIndex: Int): URL =
        wrapped.url(columnIndex)

    override fun url(columnLabel: String): URL =
        wrapped.url(columnLabel)

    override fun urlOrNull(columnIndex: Int): URL? =
        wrapped.urlOrNull(columnIndex)

    override fun urlOrNull(columnLabel: String): URL? =
        wrapped.urlOrNull(columnLabel)

    override fun uuid(columnLabel: String): UUID =
        wrapped.uuid(columnLabel)

    override fun uuidOrNull(columnLabel: String): UUID? =
        wrapped.uuidOrNull(columnLabel)

    override fun zonedDateTime(columnIndex: Int): ZonedDateTime =
        wrapped.zonedDateTime(columnIndex)

    override fun zonedDateTime(columnLabel: String): ZonedDateTime =
        wrapped.zonedDateTime(columnLabel)

    override fun zonedDateTimeOrNull(columnIndex: Int): ZonedDateTime? =
        wrapped.zonedDateTimeOrNull(columnIndex)

    override fun zonedDateTimeOrNull(columnLabel: String): ZonedDateTime? =
        wrapped.zonedDateTimeOrNull(columnLabel)

    override fun toMap(): Map<String, Any?> {
        val metaData = wrapped.metaDataOrNull()
        return (1..metaData.columnCount).associate { columnIndex ->
            metaData.getColumnLabel(columnIndex) to anyOrNull(columnIndex)
        }
    }
}
