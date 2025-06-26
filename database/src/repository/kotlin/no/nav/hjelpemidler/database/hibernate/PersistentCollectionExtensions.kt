package no.nav.hjelpemidler.database.hibernate

import org.hibernate.collection.spi.PersistentCollection

fun <E> Collection<E>.toSafeList(): List<E> =
    if (this is PersistentCollection<*> && wasInitialized()) toList() else emptyList()

fun <E> Collection<E>.toSafeSet(): Set<E> =
    if (this is PersistentCollection<*> && wasInitialized()) toSet() else emptySet()

fun <K, V> Map<K, V>.toSafeMap(): Map<K, V> =
    if (this is PersistentCollection<*> && wasInitialized()) toMap() else emptyMap()
