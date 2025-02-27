package no.nav.hjelpemidler.collections

import java.util.EnumMap

inline fun <reified K : Enum<K>, V> enumMapOf(vararg pairs: Pair<K, V>): Map<K, V> =
    EnumMap<K, V>(K::class.java).apply { putAll(pairs) }

@Suppress("UNCHECKED_CAST")
fun <K, V> Map<K, V?>.filterNotNull(): Map<K, V> =
    filterNot { it.value == null } as Map<K, V>
