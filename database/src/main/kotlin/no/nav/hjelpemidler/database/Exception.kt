package no.nav.hjelpemidler.database

class OptimisticLockException(message: String) : ConcurrentModificationException(message)

fun optimisticLockError(message: String): Nothing = throw OptimisticLockException(message)
