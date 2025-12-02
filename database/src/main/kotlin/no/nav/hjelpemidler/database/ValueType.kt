package no.nav.hjelpemidler.database

import com.fasterxml.jackson.core.type.TypeReference
import no.nav.hjelpemidler.domain.id.Id
import no.nav.hjelpemidler.domain.id.LongId
import no.nav.hjelpemidler.domain.id.StringId
import java.math.BigDecimal
import java.math.BigInteger
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.OffsetDateTime
import java.time.OffsetTime
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.full.isSubclassOf
import kotlin.reflect.full.primaryConstructor

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

internal fun <T : LongId> lagId(value: Long, type: KClass<T>): T = primaryConstructorFor(type).call(value)
internal fun <T : StringId> lagId(value: String, type: KClass<T>): T = primaryConstructorFor(type).call(value)

@Suppress("UNCHECKED_CAST")
private fun <T : Id<*>> primaryConstructorFor(type: KClass<T>) = idConstructorCache.computeIfAbsent(type) {
    type.primaryConstructor ?: error("Fant ikke primaryConstructor for ${type.qualifiedName}")
} as KFunction<T>

private val idConstructorCache: MutableMap<KClass<*>, KFunction<*>> = ConcurrentHashMap()
