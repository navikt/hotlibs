package no.nav.hjelpemidler.database.repository

import no.nav.hjelpemidler.domain.ValueType

sealed class EntityId<T : Any>(val value: T)

class StringEntityId(value: String) : EntityId<String>(value)
class LongEntityId(value: Long) : EntityId<Long>(value)
class UnknownEntityId(value: Any) : EntityId<Any>(value)

internal fun <ID : Any> ID.unwrap(): Any = if (this is ValueType<*>) value else this
