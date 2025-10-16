package no.nav.hjelpemidler.database

import com.fasterxml.jackson.databind.JsonNode
import no.nav.hjelpemidler.collections.toEnumSet
import no.nav.hjelpemidler.domain.enhet.Enhet
import no.nav.hjelpemidler.domain.enhet.Enhetsnummer
import no.nav.hjelpemidler.domain.geografi.Bydel
import no.nav.hjelpemidler.domain.geografi.Kommune
import no.nav.hjelpemidler.domain.person.AktørId
import no.nav.hjelpemidler.domain.person.Fødselsnummer
import no.nav.hjelpemidler.domain.person.Personnavn
import no.nav.hjelpemidler.serialization.jackson.add
import no.nav.hjelpemidler.serialization.jackson.jsonMapper
import no.nav.hjelpemidler.serialization.jackson.jsonToTree
import no.nav.hjelpemidler.serialization.jackson.jsonToValue
import no.nav.hjelpemidler.serialization.jackson.put
import no.nav.hjelpemidler.serialization.jackson.treeToValueOrNull
import java.io.InputStream
import java.math.BigDecimal
import java.math.BigInteger
import java.sql.Blob
import java.sql.Clob
import java.sql.ResultSet
import java.sql.ResultSetMetaData
import java.sql.Types
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.OffsetDateTime
import java.time.OffsetTime
import java.time.ZonedDateTime
import java.util.UUID
import kotlin.reflect.KClass

/**
 * Erstatning av [kotliquery.Row] for å legge til flere funksjoner og for å ikke eksponere kotliquery ut av
 * hotlibs/database.
 * Noen funksjoner fra [kotliquery.Row] er med vilje utelatt her, bla. funksjoner for Joda-Time
 * og java.sql.Date / java.sql.Time / java.sql.Timestamp. Vi benytter kun java.time.* (Java Time / JSR 310).
 */
class Row(private val resultSet: ResultSet) : DatabaseRecord, AutoCloseable by resultSet {
    val metaData: ResultSetMetaData get() = resultSet.metaData

    fun any(columnIndex: Int): Any = anyOrNull(columnIndex)!!
    fun any(columnLabel: String): Any = anyOrNull(columnLabel)!!
    fun anyOrNull(columnIndex: Int): Any? = nullable(resultSet.getObject(columnIndex))
    fun anyOrNull(columnLabel: String): Any? = nullable(resultSet.getObject(columnLabel))

    fun <T : Any> any(columnIndex: Int, type: KClass<T>): T = anyOrNull(columnIndex, type)!!
    fun <T : Any> any(columnLabel: String, type: KClass<T>): T = anyOrNull(columnLabel, type)!!

    fun <T : Any> anyOrNull(columnIndex: Int, type: KClass<T>): T? =
        nullable(resultSet.getObject<T>(columnIndex, type.java))

    fun <T : Any> anyOrNull(columnLabel: String, type: KClass<T>): T? =
        nullable(resultSet.getObject<T>(columnLabel, type.java))

    inline fun <reified T> array(columnIndex: Int): Array<T> = arrayOrNull<T>(columnIndex)!!
    inline fun <reified T> array(columnLabel: String): Array<T> = arrayOrNull<T>(columnLabel)!!

    inline fun <reified T> arrayOrNull(columnIndex: Int): Array<T>? {
        val result = sqlArrayOrNull(columnIndex)?.array as Array<*>? ?: return null
        return result.map { it as T }.toTypedArray<T>()
    }

    inline fun <reified T> arrayOrNull(columnLabel: String): Array<T>? {
        val result = sqlArrayOrNull(columnLabel)?.array as Array<*>? ?: return null
        return result.map { it as T }.toTypedArray<T>()
    }

    fun asciiStream(columnIndex: Int): InputStream = asciiStreamOrNull(columnIndex)!!
    fun asciiStream(columnLabel: String): InputStream = asciiStreamOrNull(columnLabel)!!
    fun asciiStreamOrNull(columnIndex: Int): InputStream? = nullable(resultSet.getAsciiStream(columnIndex))
    fun asciiStreamOrNull(columnLabel: String): InputStream? = nullable(resultSet.getAsciiStream(columnLabel))

    fun bigDecimal(columnIndex: Int): BigDecimal = bigDecimalOrNull(columnIndex)!!
    fun bigDecimal(columnLabel: String): BigDecimal = bigDecimalOrNull(columnLabel)!!
    fun bigDecimalOrNull(columnIndex: Int): BigDecimal? = nullable(resultSet.getBigDecimal(columnIndex))
    fun bigDecimalOrNull(columnLabel: String): BigDecimal? = nullable(resultSet.getBigDecimal(columnLabel))

    fun binaryStream(columnIndex: Int): InputStream = binaryStreamOrNull(columnIndex)!!
    fun binaryStream(columnLabel: String): InputStream = binaryStreamOrNull(columnLabel)!!
    fun binaryStreamOrNull(columnIndex: Int): InputStream? = nullable(resultSet.getBinaryStream(columnIndex))
    fun binaryStreamOrNull(columnLabel: String): InputStream? = nullable(resultSet.getBinaryStream(columnLabel))

    fun blob(columnIndex: Int): Blob = blobOrNull(columnIndex)!!
    fun blob(columnLabel: String): Blob = blobOrNull(columnLabel)!!
    fun blobOrNull(columnIndex: Int): Blob? = nullable(resultSet.getBlob(columnIndex))
    fun blobOrNull(columnLabel: String): Blob? = nullable(resultSet.getBlob(columnLabel))

    fun boolean(columnIndex: Int): Boolean = booleanOrNull(columnIndex)!!
    fun boolean(columnLabel: String): Boolean = booleanOrNull(columnLabel)!!
    fun booleanOrNull(columnIndex: Int): Boolean? = nullable(resultSet.getBoolean(columnIndex))
    fun booleanOrNull(columnLabel: String): Boolean? = nullable(resultSet.getBoolean(columnLabel))

    fun byte(columnIndex: Int): Byte = byteOrNull(columnIndex)!!
    fun byte(columnLabel: String): Byte = byteOrNull(columnLabel)!!
    fun byteOrNull(columnIndex: Int): Byte? = nullable(resultSet.getByte(columnIndex))
    fun byteOrNull(columnLabel: String): Byte? = nullable(resultSet.getByte(columnLabel))

    fun bytes(columnIndex: Int): ByteArray = bytesOrNull(columnIndex)!!
    fun bytes(columnLabel: String): ByteArray = bytesOrNull(columnLabel)!!
    fun bytesOrNull(columnIndex: Int): ByteArray? = nullable(resultSet.getBytes(columnIndex))
    fun bytesOrNull(columnLabel: String): ByteArray? = nullable(resultSet.getBytes(columnLabel))

    fun clob(columnIndex: Int): Clob = clobOrNull(columnIndex)!!
    fun clob(columnLabel: String): Clob = clobOrNull(columnLabel)!!
    fun clobOrNull(columnIndex: Int): Clob? = nullable(resultSet.getClob(columnIndex))
    fun clobOrNull(columnLabel: String): Clob? = nullable(resultSet.getClob(columnLabel))

    fun double(columnIndex: Int): Double = doubleOrNull(columnIndex)!!
    fun double(columnLabel: String): Double = doubleOrNull(columnLabel)!!
    fun doubleOrNull(columnIndex: Int): Double? = nullable(resultSet.getDouble(columnIndex))
    fun doubleOrNull(columnLabel: String): Double? = nullable(resultSet.getDouble(columnLabel))

    inline fun <reified E : Enum<E>> enum(columnIndex: Int): E = enumOrNull<E>(columnIndex)!!
    inline fun <reified E : Enum<E>> enum(columnLabel: String): E = enumOrNull<E>(columnLabel)!!

    inline fun <reified E : Enum<E>> enumOrNull(columnIndex: Int): E? =
        stringOrNull(columnIndex)?.let { enumValueOf<E>(it) }

    inline fun <reified E : Enum<E>> enumOrNull(columnLabel: String): E? =
        stringOrNull(columnLabel)?.let { enumValueOf<E>(it) }

    inline fun <reified E : Enum<E>> enums(columnIndex: Int): Set<E> = enumsOrNull<E>(columnIndex)!!
    inline fun <reified E : Enum<E>> enums(columnLabel: String): Set<E> = enumsOrNull<E>(columnLabel)!!

    inline fun <reified E : Enum<E>> enumsOrNull(columnIndex: Int): Set<E>? =
        arrayOrNull<String>(columnIndex)?.toEnumSet()

    inline fun <reified E : Enum<E>> enumsOrNull(columnLabel: String): Set<E>? =
        arrayOrNull<String>(columnLabel)?.toEnumSet()

    fun float(columnIndex: Int): Float = floatOrNull(columnIndex)!!
    fun float(columnLabel: String): Float = floatOrNull(columnLabel)!!
    fun floatOrNull(columnIndex: Int): Float? = nullable(resultSet.getFloat(columnIndex))
    fun floatOrNull(columnLabel: String): Float? = nullable(resultSet.getFloat(columnLabel))

    fun int(columnIndex: Int): Int = intOrNull(columnIndex)!!
    fun int(columnLabel: String): Int = intOrNull(columnLabel)!!
    fun intOrNull(columnIndex: Int): Int? = nullable(resultSet.getInt(columnIndex))
    fun intOrNull(columnLabel: String): Int? = nullable(resultSet.getInt(columnLabel))

    fun long(columnIndex: Int): Long = longOrNull(columnIndex)!!
    fun long(columnLabel: String): Long = longOrNull(columnLabel)!!
    fun longOrNull(columnIndex: Int): Long? = nullable(resultSet.getLong(columnIndex))
    fun longOrNull(columnLabel: String): Long? = nullable(resultSet.getLong(columnLabel))

    fun short(columnIndex: Int): Short = shortOrNull(columnIndex)!!
    fun short(columnLabel: String): Short = shortOrNull(columnLabel)!!
    fun shortOrNull(columnIndex: Int): Short? = nullable(resultSet.getShort(columnIndex))
    fun shortOrNull(columnLabel: String): Short? = nullable(resultSet.getShort(columnLabel))

    fun sqlArray(columnIndex: Int): java.sql.Array = sqlArrayOrNull(columnIndex)!!
    fun sqlArray(columnLabel: String): java.sql.Array = sqlArrayOrNull(columnLabel)!!
    fun sqlArrayOrNull(columnIndex: Int): java.sql.Array? = nullable(resultSet.getArray(columnIndex))
    fun sqlArrayOrNull(columnLabel: String): java.sql.Array? = nullable(resultSet.getArray(columnLabel))

    fun string(columnIndex: Int): String = stringOrNull(columnIndex)!!
    fun string(columnLabel: String): String = stringOrNull(columnLabel)!!
    fun stringOrNull(columnIndex: Int): String? = nullable(resultSet.getString(columnIndex))
    fun stringOrNull(columnLabel: String): String? = nullable(resultSet.getString(columnLabel))

    fun uuid(columnLabel: String): UUID = uuidOrNull(columnLabel)!!
    fun uuid(columnIndex: Int): UUID = uuidOrNull(columnIndex)!!
    fun uuidOrNull(columnIndex: Int): UUID? = anyOrNull(columnIndex, UUID::class)
    fun uuidOrNull(columnLabel: String): UUID? = anyOrNull(columnLabel, UUID::class)

    // START Java Time / JSR 310

    fun instant(columnIndex: Int): Instant = instantOrNull(columnIndex)!!
    fun instant(columnLabel: String): Instant = instantOrNull(columnLabel)!!
    fun instantOrNull(columnIndex: Int): Instant? = offsetDateTimeOrNull(columnIndex)?.toInstant()
    fun instantOrNull(columnLabel: String): Instant? = offsetDateTimeOrNull(columnLabel)?.toInstant()

    fun localDate(columnIndex: Int): LocalDate = localDateOrNull(columnIndex)!!
    fun localDate(columnLabel: String): LocalDate = localDateOrNull(columnLabel)!!
    fun localDateOrNull(columnIndex: Int): LocalDate? = anyOrNull(columnIndex, LocalDate::class)
    fun localDateOrNull(columnLabel: String): LocalDate? = anyOrNull(columnLabel, LocalDate::class)

    fun localTime(columnIndex: Int): LocalTime = localTimeOrNull(columnIndex)!!
    fun localTime(columnLabel: String): LocalTime = localTimeOrNull(columnLabel)!!
    fun localTimeOrNull(columnIndex: Int): LocalTime? = anyOrNull(columnIndex, LocalTime::class)
    fun localTimeOrNull(columnLabel: String): LocalTime? = anyOrNull(columnLabel, LocalTime::class)

    fun localDateTime(columnIndex: Int): LocalDateTime = localDateTimeOrNull(columnIndex)!!
    fun localDateTime(columnLabel: String): LocalDateTime = localDateTimeOrNull(columnLabel)!!
    fun localDateTimeOrNull(columnIndex: Int): LocalDateTime? = anyOrNull(columnIndex, LocalDateTime::class)
    fun localDateTimeOrNull(columnLabel: String): LocalDateTime? = anyOrNull(columnLabel, LocalDateTime::class)

    fun offsetTime(columnIndex: Int): OffsetTime = offsetTimeOrNull(columnIndex)!!
    fun offsetTime(columnLabel: String): OffsetTime = offsetTimeOrNull(columnLabel)!!
    fun offsetTimeOrNull(columnIndex: Int): OffsetTime? = anyOrNull(columnIndex, OffsetTime::class)
    fun offsetTimeOrNull(columnLabel: String): OffsetTime? = anyOrNull(columnLabel, OffsetTime::class)

    fun offsetDateTime(columnIndex: Int): OffsetDateTime = offsetDateTimeOrNull(columnIndex)!!
    fun offsetDateTime(columnLabel: String): OffsetDateTime = offsetDateTimeOrNull(columnLabel)!!
    fun offsetDateTimeOrNull(columnIndex: Int): OffsetDateTime? = anyOrNull(columnIndex, OffsetDateTime::class)
    fun offsetDateTimeOrNull(columnLabel: String): OffsetDateTime? = anyOrNull(columnLabel, OffsetDateTime::class)

    fun zonedDateTime(columnIndex: Int): ZonedDateTime = zonedDateTimeOrNull(columnIndex)!!
    fun zonedDateTime(columnLabel: String): ZonedDateTime = zonedDateTimeOrNull(columnLabel)!!
    fun zonedDateTimeOrNull(columnIndex: Int): ZonedDateTime? = offsetDateTimeOrNull(columnIndex)?.toZonedDateTime()
    fun zonedDateTimeOrNull(columnLabel: String): ZonedDateTime? = offsetDateTimeOrNull(columnLabel)?.toZonedDateTime()

    // END Java Time / JSR 310

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
        val metaData = this.metaData
        return (1..metaData.columnCount).associate { columnIndex ->
            metaData.getColumnLabel(columnIndex) to anyOrNull(columnIndex)
        }
    }

    // START JSON

    inline fun <reified T> json(columnIndex: Int): T = jsonOrNull<T>(columnIndex)!!
    inline fun <reified T> json(columnLabel: String): T = jsonOrNull<T>(columnLabel)!!
    inline fun <reified T> jsonOrNull(columnIndex: Int): T? = stringOrNull(columnIndex)?.let { jsonToValue<T>(it) }
    inline fun <reified T> jsonOrNull(columnLabel: String): T? = stringOrNull(columnLabel)?.let { jsonToValue<T>(it) }

    fun tree(columnIndex: Int): JsonNode = treeOrNull(columnIndex)!!
    fun tree(columnLabel: String): JsonNode = treeOrNull(columnLabel)!!
    fun treeOrNull(columnIndex: Int): JsonNode? = stringOrNull(columnIndex)?.let { jsonToTree(it) }
    fun treeOrNull(columnLabel: String): JsonNode? = stringOrNull(columnLabel)?.let { jsonToTree(it) }

    fun toTree(): JsonNode {
        val metaData = this.metaData
        val rootNode = jsonMapper.createObjectNode()
        (1..metaData.columnCount).forEach { columnIndex ->
            val propertyName = metaData.getColumnLabel(columnIndex)
            val value = when (metaData.getColumnType(columnIndex)) {
                // Datetime
                Types.DATE -> localDateOrNull(columnIndex)

                Types.TIME -> when (metaData.getColumnTypeName(columnIndex)) {
                    "timetz" -> offsetTimeOrNull(columnIndex)
                    else -> localTimeOrNull(columnIndex)
                }

                Types.TIMESTAMP -> when (metaData.getColumnTypeName(columnIndex)) {
                    "timestamptz" -> offsetDateTimeOrNull(columnIndex)
                    else -> localDateTimeOrNull(columnIndex)
                }

                // Egendefinerte typer (e.g. CREATE TYPE / DOMAIN i PostgreSQL)
                Types.STRUCT -> null

                // Array
                Types.ARRAY -> {
                    val array = arrayOrNull<Any?>(columnIndex)
                    if (array == null) {
                        null
                    } else {
                        val arrayNode = rootNode.arrayNode(array.size)
                        array.forEach(arrayNode::add)
                        arrayNode
                    }
                }

                // JSON
                Types.OTHER -> when (metaData.getColumnTypeName(columnIndex)) {
                    "json", "jsonb" -> treeOrNull(columnIndex)
                    else -> anyOrNull(columnIndex)
                }

                // Fallback til JDBC-definert mapping til Java
                else -> anyOrNull(columnIndex)
            }
            if (value is JsonNode) {
                rootNode.set(propertyName, value)
            } else {
                rootNode.put(propertyName, value)
            }
        }
        // Hvis kun én kolonne returnerer vi kun denne ene verdien
        return if (metaData.columnCount == 1) {
            rootNode.values().next()
        } else {
            rootNode
        }
    }

    fun <T : Any> toValue(type: KClass<T>): T = toValueOrNull(type)!!
    inline fun <reified T : Any> toValue(): T = toValueOrNull<T>()!!

    /**
     * @see [isValueType]
     */
    fun <T : Any> toValueOrNull(type: KClass<T>): T? {
        return if (type.isValueType) {
            anyOrNull(1, type)
        } else {
            treeToValueOrNull(toTree(), type)
        }
    }

    /**
     * @see [isValueType]
     */
    inline fun <reified T : Any> toValueOrNull(): T? {
        val type = T::class
        return if (type.isValueType) {
            anyOrNull(1, type)
        } else {
            treeToValueOrNull<T>(toTree(), type) // fixme
        }
    }

    // END JSON

    // START DOMAIN

    fun aktørId(columnLabel: String): AktørId = aktørIdOrNull(columnLabel)!!
    fun aktørIdOrNull(columnLabel: String): AktørId? = stringOrNull(columnLabel)?.let(::AktørId)

    fun fødselsnummer(columnLabel: String): Fødselsnummer = fødselsnummerOrNull(columnLabel)!!
    fun fødselsnummerOrNull(columnLabel: String): Fødselsnummer? = stringOrNull(columnLabel)?.let(::Fødselsnummer)

    fun enhetsnummer(columnLabel: String): Enhetsnummer = enhetsnummerOrNull(columnLabel)!!
    fun enhetsnummerOrNull(columnLabel: String): Enhetsnummer? = stringOrNull(columnLabel)?.let(::Enhetsnummer)

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

    // END DOMAIN

    private fun <T> nullable(value: T): T? = if (resultSet.wasNull()) null else value
}

val KClass<*>.isValueType: Boolean
    get() = when (this) {
        BigDecimal::class,
        BigInteger::class,
        Boolean::class,
        Byte::class,
        ByteArray::class,
        Double::class,
        Float::class,
        Int::class,
        Long::class,
        Short::class,
        String::class,
        UUID::class,

        Instant::class,
        LocalDate::class,
        LocalDateTime::class,
        LocalTime::class,
        OffsetDateTime::class,
        OffsetTime::class,
            -> true

        else -> false
    }
