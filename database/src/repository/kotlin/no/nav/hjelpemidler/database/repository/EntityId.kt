package no.nav.hjelpemidler.database.repository

sealed class EntityId<T : Any>(val value: T)

class StringEntityId(value: String) : EntityId<String>(value)
class LongEntityId(value: Long) : EntityId<Long>(value)
class UnknownEntityId(value: Any) : EntityId<Any>(value)
