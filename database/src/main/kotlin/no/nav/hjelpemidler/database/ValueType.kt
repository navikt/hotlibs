package no.nav.hjelpemidler.database

import com.fasterxml.jackson.core.type.TypeReference
import no.nav.hjelpemidler.domain.id.Id
import java.math.BigDecimal
import java.math.BigInteger
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.OffsetDateTime
import java.time.OffsetTime
import java.util.UUID
import kotlin.reflect.KClass
import kotlin.reflect.full.isSubclassOf

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

val KClass<*>.isIdType: Boolean
    get() = isSubclassOf(Id::class)

val TypeReference<*>.isValueType: Boolean
    get() {
        val type = this.type as? Class<*> ?: return false
        return type.kotlin.isValueType
    }

val TypeReference<*>.isIdType: Boolean
    get() {
        val type = this.type as? Class<*> ?: return false
        return type.kotlin.isIdType
    }
