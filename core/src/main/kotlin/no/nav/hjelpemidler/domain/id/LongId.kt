package no.nav.hjelpemidler.domain.id

abstract class LongId(value: Long) : Id<Long>(value)

fun Iterable<LongId>.toLongArray(): LongArray = map(LongId::value).toLongArray()
